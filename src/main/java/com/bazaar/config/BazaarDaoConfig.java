package com.bazaar.config;

import com.bazaar.dao.BazaarDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**\n * Database Configuration for Railway & Local Environments\n *\n * Environment Variable Mapping:\n *    BAZAAR_DB_URL       \u2192 bazaar.db.url       (JDBC connection string)\n *    BAZAAR_DB_USERNAME  \u2192 bazaar.db.username  (Database user)\n *    BAZAAR_DB_PASSWORD  \u2192 bazaar.db.password  (Database password)\n *\n * Configuration Sources:\n *    - Local Development:\n *        application.properties reads from system environment variables\n *        Set using: $env:BAZAAR_DB_URL=\"jdbc:mysql://...\"\n *\n *    - Railway Production:\n *        Application receives variables from Railway's service environment\n *        Set in Railway console under Java Service \u2192 Variables tab\n *        Example BAZAAR_DB_URL: jdbc:mysql://mysql.railway.internal:3306/railway?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC\n *\n * Critical Notes for Railway:\n *    - Use \"mysql.railway.internal\" as the host (internal Railway network)\n *    - Include query parameters: ?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC\n *    - Database name is typically \"railway\" (created automatically by Railway MySQL service)\n *    - Username is typically \"root\" (default Railway MySQL user)\n *\n * Bean Creation:\n *    - Spring @Value annotation auto-injects environment variable values\n *    - BazaarDAO is instantiated with resolved credentials\n *    - Single instance available for injection across entire application\n */\n@Configuration\npublic class BazaarDaoConfig {

    @Bean
    public BazaarDAO bazaarDAO(
            @Value("${bazaar.db.url}") String url,
            @Value("${bazaar.db.username}") String username,
            @Value("${bazaar.db.password}") String password
    ) {
        return new BazaarDAO(url, username, password);
    }
}
