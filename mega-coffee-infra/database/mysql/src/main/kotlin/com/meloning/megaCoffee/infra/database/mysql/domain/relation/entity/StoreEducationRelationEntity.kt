package com.meloning.megaCoffee.infra.database.mysql.domain.relation.entity

import com.meloning.megaCoffee.core.domain.relation.model.StoreEducationRelation
import com.meloning.megaCoffee.infra.database.mysql.domain.common.CreatedAtEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.education.entity.EducationEntity
import org.hibernate.proxy.HibernateProxy
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "store_education_relation")
class StoreEducationRelationEntity : CreatedAtEntity {

    constructor(id: Long?, education: EducationEntity, storeId: Long) : super() {
        this.id = id
        this.education = education
        this.storeId = storeId
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id")
    val education: EducationEntity

    val storeId: Long

    fun toModel(): StoreEducationRelation = StoreEducationRelation(
        id = id!!,
        storeId = storeId,
        education = education.toModel(),
        createdAt = createdAt
    )

    companion object {
        @JvmStatic
        fun from(model: StoreEducationRelation) = with(model) {
            StoreEducationRelationEntity(
                id = id,
                storeId = storeId,
                education = EducationEntity.from(education)
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
