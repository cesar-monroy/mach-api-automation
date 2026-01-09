package com.mach.api.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for Account API.
 * Represents the response received after creating a user account.
 * 
 * Response structure:
 * {
 *     "machId": "c9be9277-b215-4afe-b464-e5e9513ead80",
 *     "documentNumber": "409217605",
 *     "email": "Vivien.Rolfson4@example.org",
 *     "phone": "56955387496"
 * }
 */
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

