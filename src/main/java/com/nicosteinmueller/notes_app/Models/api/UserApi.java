package com.nicosteinmueller.notes_app.Models.api;

import com.nicosteinmueller.notes_app.Models.Settings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserApi {
    private String firstName;
    private String lastName;
    private String email;
    private Settings settings;
    private List<String> labels;
}
