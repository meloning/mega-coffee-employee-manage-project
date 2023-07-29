package com.meloning.megaCoffee.infra.database.mysql.domain.education.entity

import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.infra.database.mysql.domain.common.BaseTimeEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.proxy.HibernateProxy
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(name = "education")
@DynamicInsert
@DynamicUpdate
class EducationEntity : BaseTimeEntity {

    constructor(id: Long?, name: NameVO, content: String, targetTypes: MutableList<EmployeeType>) : super() {
        this.id = id
        this.name = name
        this.content = content
        this.targetTypes = targetTypes
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Embedded
    val name: NameVO

    @Lob
    @Column(nullable = false)
    var content: String
        protected set

    @ElementCollection
    @Enumerated(value = EnumType.STRING)
    @CollectionTable(name = "education_employee_type", joinColumns = [JoinColumn(name = "education_id")])
    @Column(name = "employee_type", nullable = false)
    var targetTypes: MutableList<EmployeeType> = mutableListOf()
        protected set

    @Embedded
    var educationAddresses: EducationAddressesVO = EducationAddressesVO(mutableListOf())
        protected set

    fun update(educationAddressesVO: EducationAddressesVO) {
        this.educationAddresses = educationAddressesVO
    }

    fun toModel() = Education(
        id = id,
        name = name.toModel(),
        content = content,
        targetTypes = targetTypes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        @JvmStatic
        fun from(model: Education) = with(model) {
            EducationEntity(
                id = id,
                name = NameVO.from(name),
                content = content,
                targetTypes = targetTypes
            )
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass = if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass = if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as EducationEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = id.hashCode()
}
