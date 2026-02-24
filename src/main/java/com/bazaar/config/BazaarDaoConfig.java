package com.bazaar.config;

import com.bazaar.dao.BazaarDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BazaarDaoConfig {

    @Bean
    public BazaarDAO bazaarDAO(
            @Value("${bazaar.db.url}") String url,
            @Value("${bazaar.db.username}") String username,
            @Value("${bazaar.db.password}") String password
    ) {
        return new BazaarDAO(url, username, password);
    }
}
