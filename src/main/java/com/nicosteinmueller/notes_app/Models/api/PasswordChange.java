package com.nicosteinmueller.notes_app.Models.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PasswordChange {
    private String oldPassword;
    private String newPassword;
}
