package com.arkaces.aces_marketplace_api.registration;

import lombok.Data;

@Data
public class Registration {
    private String id;
    private String createdAt;
    private Account account;
}
