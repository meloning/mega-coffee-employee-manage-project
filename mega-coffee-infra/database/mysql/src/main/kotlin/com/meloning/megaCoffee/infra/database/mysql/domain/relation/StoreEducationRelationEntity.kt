package com.meloning.megaCoffee.infra.database.mysql.domain.relation

import com.meloning.megaCoffee.infra.database.mysql.domain.common.CreatedAtEntity
import org.hibernate.proxy.HibernateProxy
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "store_education_relation")
class StoreEducationRelationEntity : CreatedAtEntity {

    constructor(id: Long?, storeId: Long, educationId: Long) : super() {
        this.id = id
        this.storeId = storeId
        this.educationId = educationId
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    val storeId: Long

    val educationId: Long

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass = if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass = if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as StoreEducationRelationEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = id.hashCode()
}
