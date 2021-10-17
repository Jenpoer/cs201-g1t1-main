package com.cs201.g1t1;

import java.io.IOException;

import com.cs201.g1t1.util.DatabaseImport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class G1T1ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(G1T1ProjectApplication.class, args);
	}

	// @Bean
	// CommandLineRunner registerZonesDataRunner(DatabaseImport databaseImport) {
	// 	try {
	// 		databaseImport.importBusiness();
	// 	} catch (IOException e) {

	// 	}
	// }

}
