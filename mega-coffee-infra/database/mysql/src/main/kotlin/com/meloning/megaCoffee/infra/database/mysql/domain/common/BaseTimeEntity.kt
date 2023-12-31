package com.meloning.megaCoffee.infra.database.mysql.domain.common

import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseTimeEntity : CreatedAtEntity() {
    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
        protected set
}
