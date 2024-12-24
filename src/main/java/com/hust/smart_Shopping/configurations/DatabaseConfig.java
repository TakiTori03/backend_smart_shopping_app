package com.hust.smart_Shopping.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({ "com.hust.smart_Shopping.repositories" })
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfig {

}
