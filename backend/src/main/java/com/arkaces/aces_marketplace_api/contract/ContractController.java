package com.arkaces.aces_marketplace_api.contract;

import com.arkaces.aces_marketplace_api.account.AccountEntity;
import com.arkaces.aces_marketplace_api.account.AccountRepository;
import com.arkaces.aces_marketplace_api.common.IdentifierGenerator;
import com.arkaces.aces_marketplace_api.error.*;
import com.arkaces.aces_marketplace_api.security.AuthenticatedUser;
import com.arkaces.aces_marketplace_api.service_client.RemoteContractResponse;
import com.arkaces.aces_marketplace_api.service_client.ServiceClient;
import com.arkaces.aces_marketplace_api.services.ServiceEntity;
import com.arkaces.aces_marketplace_api.services.ServiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ContractController {
    
    private final AccountRepository accountRepository;
    private final ServiceRepository serviceRepository;
    private final ContractRepository contractRepository;
    private final ServiceClient serviceClient;
    private final IdentifierGenerator identifierGenerator;
    private final ContractMapper contractMapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @PostMapping("/services/{serviceId}/contracts")
    public Contract createContract(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
        @PathVariable String serviceId,
        @RequestBody CreateContractRequest createContractRequest
    ) {

        AccountEntity accountEntity = null;

        if(authenticatedUser != null){
            accountEntity = accountRepository.findOne(authenticatedUser.getAccountPid());
        }


        ServiceEntity serviceEntity = serviceRepository.findOneById(serviceId);
        if (serviceEntity == null) {
            throw new NotFoundException(ErrorCodes.NOT_FOUND, "Service not found");
        }
        
        String serviceUrl = serviceEntity.getUrl(); // todo: right trim any "/"
        String url = serviceUrl + "/contracts";
        
        String id = identifierGenerator.generate();

        String argumentsJson;
        try {
            argumentsJson = objectMapper.writeValueAsString(createContractRequest.getArguments());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to write contract arguments", e);
        }

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(id);
        contractEntity.setStatus("new");
        contractEntity.setCreatedAt(LocalDateTime.now());
        contractEntity.setArgumentsJson(argumentsJson);
        contractEntity.setAccountEntity(accountEntity);
        contractEntity.setServiceEntity(serviceEntity);
        contractRepository.save(contractEntity);

        RemoteContractResponse remoteContractResponse;
        try {
            remoteContractResponse = serviceClient.createContract(url, id, createContractRequest.getArguments());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                // Pass through validation errors
                try {
                    ValidationError validationError = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);
                    throw new ValidationException(validationError.getCode(), validationError.getMessage(), validationError.getFieldErrors());
                } catch (IOException e1) {
                    throw new RuntimeException("Failed to parse validation error json", e1);
                }
            } else {
                throw new ValidationException(ErrorCodes.SERVICE_CONTRACT_REQUEST_FAILED, "Failed to create service contract", e);
            }
        } catch (Exception e) {
            throw new ValidationException(ErrorCodes.SERVICE_CONTRACT_REQUEST_FAILED, "Failed to create service contract", e);
        }
        
        contractEntity.setRemoteContractId(remoteContractResponse.getId());
        contractEntity.setStatus(remoteContractResponse.getStatus());
        contractRepository.save(contractEntity);

        return contractMapper.map(contractEntity, remoteContractResponse);
    }

    @GetMapping("/contracts/{contractId}")
    public Contract getContract(
            @PathVariable String contractId
    ) {
        ContractEntity contractEntity = contractRepository.findOneById(contractId);
        if (contractEntity == null) {
            throw new NotFoundException(ErrorCodes.NOT_FOUND, "Contract not found");
        }

        String serviceUrl = contractEntity.getServiceEntity().getUrl();
        String url = serviceUrl + "/contracts/" + contractEntity.getRemoteContractId();

        RemoteContractResponse remoteContractResponse;
        try {
            remoteContractResponse = serviceClient.getContract(url);
        } catch (Exception e) {
            throw new ValidationException(ErrorCodes.SERVICE_CONTRACT_REQUEST_FAILED, "Failed to get service contract", e);
        }

        // update status if changed
        if (! remoteContractResponse.getStatus().equals(contractEntity.getStatus())) {
            log.info("Updating contract status for contract id " + contractId + " from "
                    + contractEntity.getStatus() + " to " + remoteContractResponse.getStatus());
            contractEntity.setStatus(remoteContractResponse.getStatus());
            contractRepository.save(contractEntity);
        }

        return contractMapper.map(contractEntity, remoteContractResponse);
    }
    
}
