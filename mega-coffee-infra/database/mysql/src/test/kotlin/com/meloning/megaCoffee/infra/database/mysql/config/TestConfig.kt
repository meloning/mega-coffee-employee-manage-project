package com.meloning.megaCoffee.infra.database.mysql.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["com.meloning.megaCoffee.infra.database.mysql.domain.*.repository"])
@EntityScan(value = ["com.meloning.megaCoffee.infra.database.mysql.domain.*.entity"])
class TestConfig
