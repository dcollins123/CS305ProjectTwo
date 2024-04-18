package com.snhu.sslserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.constraints.NotBlank;						   //changed from javax due to spring boot and java upgrade
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class SslServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SslServerApplication.class, args);
	}

}
//FIXME: Add route to enable check sum return of static data example:  String data = "Hello World Check Sum!";
@RestController															//handles checksum gen requests					
class ServerController {

	@GetMapping("/hash")												//change to GetMapping to clarify http requests
	@ResponseBody
	public String myHash(@RequestParam @NotBlank String data) {
		String checksum = "";
		String algorithm = "SHA-256";								

		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);	//create instance for secured hash
			byte[] hash = md.digest(data.getBytes());					//hash computation
			checksum = bytesToHex(hash);								//converts hash to hex 
		} 
	catch (NoSuchAlgorithmException e) {								//error log
			System.out.println("Algorithm not found: " +e.getMessage());
		}

		return "<p>Data: " + data + "</p>" +							//HTML message
				"<p>Name of Cipher Algorithm: " + algorithm + "</p>" +
				"<p>Checksum: " + checksum + "</p>";
	}

	private String bytesToHex(byte[] hash) {							//convert byte to hex
		StringBuilder hexString = new StringBuilder();					//using StringBuilder instead of concat. to improve performance
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);					//makes byte unsigned, positive #'s convert
			if (hex.length() == 1) {									//ensure hex str is double digit
				hexString.append('0');
			}
			hexString.append(hex);										//add hex str 
		}
		return hexString.toString();									//returns full hex str 
	}
}