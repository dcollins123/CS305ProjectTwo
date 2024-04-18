package com.snhu.sslserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class SslServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SslServerApplication.class, args);
    }
}

@RestController
class ServerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    @GetMapping("/hash")
    public ResponseEntity<ChecksumResponse> myHash() {
        String data = "Dan Collins";
        String algorithm = "SHA-256";

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hash = md.digest(data.getBytes());
            String checksum = bytesToHex(hash);

            ChecksumResponse response = new ChecksumResponse(data, algorithm, checksum);
            return ResponseEntity.ok(response);

        } catch (NoSuchAlgorithmException e) {
            logger.error("Error generating checksum with algorithm " + algorithm, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

class ChecksumResponse {
    private String data;
    private String algorithm;
    private String checksum;

    public ChecksumResponse(String data, String algorithm, String checksum) {
        this.data = data;
        this.algorithm = algorithm;
        this.checksum = checksum;
    }

    // Getters and setters

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
