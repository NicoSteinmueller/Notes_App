package com.nicosteinmueller.notes_app.DataGeneration;

import com.nicosteinmueller.notes_app.Models.Language;
import com.nicosteinmueller.notes_app.Models.Settings;
import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Utilities.HashUtilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserGeneration {
    public static User generateUser() {
        Settings settings = new Settings(true, Language.GERMAN);
        return new User(
                "Amanda",
                "Tobes",
                HashUtilities.hashSHA512("1234567"),
                "amanda.tobes@avid.com",
                LocalDateTime.now(),
                List.of("Work", "Shopping"),
                settings);
    }

    public static List<User> generateUsers(){
        List<User> list = new ArrayList<>();
        Settings settings = new Settings(true, Language.GERMAN);
        list.add(new User(
                "Amanda",
                "Tobes",
                HashUtilities.hashSHA512("HP4Jobwjvdi45!"),
                "amanda.tobes@avid.com",
                LocalDateTime.now(),
                List.of("Work", "Shopping"),
                new Settings(true, Language.GERMAN)));
        list.add(new User(
                "Tom",
                "Meier",
                HashUtilities.hashSHA512("D3jkdw§dwdnxwoje2n!"),
                "tom.meier@avid.com",
                LocalDateTime.now(),
                List.of("Work", "Football"),
                new Settings(true, Language.ENGLISH)));
        list.add(new User(
                "Andreas",
                "Klebrig",
                HashUtilities.hashSHA512("hb$UGVvdr152718njvb7&$"),
                "a.klebrig@natgeo.de",
                LocalDateTime.now(),
                List.of("Travel", "Exploration"),
                new Settings(false, Language.GERMAN)));
        list.add(new User(
                "Addaine",
                "Abernant",
                HashUtilities.hashSHA512("Hkhvz4cD45wu75sx43)%%&5t$dUTtg"),
                "theelven@oracle.com",
                LocalDateTime.now(),
                List.of("Work", "Travel"),
                new Settings(true, Language.ENGLISH)));
        list.add(new User(
                "Mister",
                "Doctor",
                HashUtilities.hashSHA512("GKfQdgfvziZ!527CGWJf7&%"),
                "itsstrange@marvel.com",
                LocalDateTime.now(),
                List.of("Travel", "Football"),
                new Settings(true, Language.ENGLISH)));
        return list;
    }
}
