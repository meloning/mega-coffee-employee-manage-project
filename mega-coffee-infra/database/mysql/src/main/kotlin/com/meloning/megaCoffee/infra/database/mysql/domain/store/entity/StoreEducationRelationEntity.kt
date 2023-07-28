package com.meloning.megaCoffee.infra.database.mysql.domain.store.entity

import com.meloning.megaCoffee.core.domain.store.model.StoreEducationRelation
import com.meloning.megaCoffee.infra.database.mysql.domain.common.CreatedAtEntity
import org.hibernate.proxy.HibernateProxy
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "store_education_relation")
class StoreEducationRelationEntity : CreatedAtEntity {

    constructor(id: Long?, store: StoreEntity, educationId: Long) : super() {
        this.id = id
        this.store = store
        this.educationId = educationId
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: StoreEntity

    val educationId: Long

    fun toModel(): StoreEducationRelation = StoreEducationRelation(
        id = id!!,
        store = store.toModel(),
        educationId = educationId,
        createdAt = createdAt
    )

    companion object {
        @JvmStatic
        fun from(model: StoreEducationRelation) = with(model) {
            StoreEducationRelationEntity(
                id = id,
                store = StoreEntity.from(store),
                educationId = educationId
            )
        }
    }

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
