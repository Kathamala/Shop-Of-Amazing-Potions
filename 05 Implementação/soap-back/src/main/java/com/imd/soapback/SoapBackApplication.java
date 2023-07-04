package main.java.com.imd.soapback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoapBackApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/soap");
		SpringApplication.run(SoapBackApplication.class, args);
	}

}