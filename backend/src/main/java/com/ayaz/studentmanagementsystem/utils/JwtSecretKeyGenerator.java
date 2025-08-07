package com.ayaz.studentmanagementsystem.utils;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Jwts.SIG.HS512.key().build();

        // Encode the key in Base64 so it can be safely stored in a properties file
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        // Print the key to the console
        System.out.println("--- 🔑 Generated JWT Secret Key ---");
        System.out.println(base64Key);
        System.out.println("------------------------------------");
        System.out.println("Copy this key and paste it into your application.yml or application.properties file.");
    }
}