package com.arkaces.aces_marketplace_api.service_client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class ServiceResponse {
    private String name;
    private String description;
    private String version;
    private String websiteUrl;
    private Boolean isTestNet;
    private String instructions;
    private List<Capacity> capacities;
    private String flatFee;
    private String percentFee;
    private JsonNode inputSchema;
    private JsonNode outputSchema;
}


