package com.nicosteinmueller.notes_app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Settings {
    private boolean DarkModeOn;
    private Language language;
}
