package com.bazaar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BazaarServiceApplication {

    /*
     * Hosting notes (Railway platform):
     * 1) Push this project to GitHub.
     * 2) In Railway, create a new project from the GitHub repo.
     * 3) Add environment variables:
     *      BAZAAR_DB_URL
     *      BAZAAR_DB_USERNAME
     *      BAZAAR_DB_PASSWORD
     *      PORT (Railway injects this automatically in most cases)
     * 4) Build command: mvn clean package
     * 5) Start command: java -jar target/bazaar-tracker-1.0.0.jar
     * 6) Railway exposes a public URL; service endpoints are under /api/*
     */
    public static void main(String[] args) {
        SpringApplication.run(BazaarServiceApplication.class, args);
    }
}
