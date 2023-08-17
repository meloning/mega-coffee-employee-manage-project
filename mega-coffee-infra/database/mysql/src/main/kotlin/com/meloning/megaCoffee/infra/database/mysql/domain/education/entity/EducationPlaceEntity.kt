package com.meloning.megaCoffee.infra.database.mysql.domain.education.entity

import com.meloning.megaCoffee.core.domain.education.model.EducationPlace
import com.meloning.megaCoffee.infra.database.mysql.domain.common.AddressVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.BaseTimeEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.common.TimeRangeVO
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "education_place")
@DynamicInsert
@DynamicUpdate
class EducationPlaceEntity : BaseTimeEntity {

    constructor(id: Long?, education: EducationEntity, address: AddressVO, currentParticipant: Int, maxParticipant: Int, date: LocalDate, timeRange: TimeRangeVO) : super() {
        this.id = id
        this.education = education
        this.address = address
        this.currentParticipant = currentParticipant
        this.maxParticipant = maxParticipant
        this.date = date
        this.timeRange = timeRange
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id", nullable = false)
    var education: EducationEntity
        protected set

    @Embedded
    var address: AddressVO
        protected set

    @Column(nullable = false)
    var maxParticipant: Int
        protected set

    @Column(nullable = false)
    @ColumnDefault("0")
    var currentParticipant: Int = 0
        protected set

    @Column(nullable = false)
    var date: LocalDate
        protected set

    @Embedded
    var timeRange: TimeRangeVO
        protected set

    @Version
    @ColumnDefault("0")
    var version: Long = 0

    fun toModel() = EducationPlace(
        id = id,
        education = education.toModel(),
        address = address.toModel(),
        currentParticipant = currentParticipant,
        maxParticipant = maxParticipant,
        date = date,
        timeRange = timeRange.toModel(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        @JvmStatic
        fun from(model: EducationPlace) = with(model) {
            EducationPlaceEntity(
                id = id,
                education = EducationEntity.from(education),
                address = AddressVO.from(address),
                currentParticipant = currentParticipant,
                maxParticipant = maxParticipant,
                date = date,
                timeRange = TimeRangeVO.from(timeRange)
            )
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass = if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass = if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as EducationPlaceEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = id.hashCode()
}
