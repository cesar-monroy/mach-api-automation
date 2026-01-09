package com.mach.api.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private String machId;
    private String documentNumber;
    private String email;
    private String phone;
}

