package com.nicosteinmueller.notes_app.Utilities;

import java.security.MessageDigest;

public class HashUtilities {
    public static byte[] hashSHA512(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA512");
            return md.digest(input.getBytes());
        }catch (Exception ignored){}
        return null;
    }



}
