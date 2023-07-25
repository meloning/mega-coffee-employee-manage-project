package com.meloning.megaCoffee.infra.database.mysql.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.meloning.megaCoffee.infra.database.mysql.domain.*.repository"])
class JpaConfig
