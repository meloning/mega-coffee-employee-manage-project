package com.meloning.megaCoffee.infra.database.mysql.domain.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class CreatedAtEntity {
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null
        protected set
}
