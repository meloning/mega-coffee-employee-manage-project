package com.meloning.megaCoffee.infra.database.mysql.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class QuerydslConfig {

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Bean
    fun jpqQueryFactory() = JPAQueryFactory(entityManager)
}
