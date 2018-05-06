import { Component, OnInit } from '@angular/core';
import {ApiClient, Service} from '../api-client/api-client.component';
import {ErrorModalService} from '../app-components/error-modal-service.compoennt';
import {ActivatedRoute} from '@angular/router';

const SERVICES_PER_PAGE = 25;

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.scss']
})

export class ServicesComponent implements OnInit {
  loading = true;
  services: Service[] = [];
  visibleServices: Service[] = [];
  currentPage: number;
  servicesPerPage = SERVICES_PER_PAGE;

  constructor(
    private apiClient: ApiClient,
    private errorModalService: ErrorModalService,
    private route: ActivatedRoute,
  ) {
    this.currentPage = 1;
  }

  ngOnInit() {
    this.apiClient.getServices()
      .subscribe(
        response => {
          this.services = response.items;
          this.setVisibleServices();
          this.loading = false;
        },
      error => {
        console.log(error);
        this.errorModalService.showDefaultError();
        this.loading = false;
      });
  }

  setVisibleServices() {
    const start = (this.currentPage - 1) * SERVICES_PER_PAGE;
    const end = start + SERVICES_PER_PAGE;
    this.visibleServices = this.services.slice(start, end);
  }
}
