package com.meloning.megaCoffee.infra.database.mysql.config

import com.google.common.base.CaseFormat
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Table
import javax.persistence.metamodel.EntityType

@Component
class DatabaseCleanup : InitializingBean {

    @PersistenceContext
    lateinit var entityManager: EntityManager

    lateinit var tableNames: MutableList<String>

    override fun afterPropertiesSet() {
        val entities: Set<EntityType<*>> = entityManager.metamodel.entities
        tableNames = entities.filter { e -> isEntity(e) && hasTableAnnotation(e) }
            .map { e ->
                val tableName = e.javaType.getAnnotation(Table::class.java).name
                tableName.ifBlank { CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.name) }
            }
            .toMutableList()

        val entityNames = entities.filter { e -> isEntity(e) && !hasTableAnnotation(e) }
            .map { e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.name) }

        tableNames.addAll(entityNames)
    }

    private fun isEntity(e: EntityType<*>): Boolean {
        return null != e.javaType.getAnnotation(Entity::class.java)
    }

    private fun hasTableAnnotation(e: EntityType<*>): Boolean {
        return null != e.javaType.getAnnotation(Table::class.java)
    }

    @Transactional
    fun execute() {
        entityManager.flush()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()

        for (tableName in tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
            entityManager.createNativeQuery("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1").executeUpdate()
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
    }
}
