package com.meloning.megaCoffee.infra.database.mysql.domain.store.entity

import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.infra.database.mysql.domain.common.AddressVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.BaseTimeEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.TimeRangeVO
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.SQLDelete
import org.hibernate.proxy.HibernateProxy
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "store")
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update store set is_deleted = true where id = ?")
class StoreEntity : BaseTimeEntity {

    constructor(id: Long?, name: NameVO, type: StoreType, ownerId: Long?, address: AddressVO, timeRange: TimeRangeVO, deleted: Boolean) : super() {
        this.id = id
        this.name = name
        this.type = type
        this.ownerId = ownerId
        this.address = address
        this.timeRange = timeRange
        this.deleted = deleted
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Embedded
    val name: NameVO

    @Enumerated(value = EnumType.STRING)
    var type: StoreType
        protected set

    var ownerId: Long? = null
        protected set

    @Embedded
    var address: AddressVO
        protected set

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "startTime", column = Column(name = "opening_time")),
            AttributeOverride(name = "endTime", column = Column(name = "closing_time")),
        ]
    )
    var timeRange: TimeRangeVO
        protected set

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault(value = "0")
    var deleted: Boolean = false
        protected set

    fun toModel() = Store(
        id = id,
        name = name.toModel(),
        type = type,
        ownerId = ownerId,
        address = address.toModel(),
        timeRange = timeRange.toModel(),
        deleted = deleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        @JvmStatic
        fun from(model: Store) = with(model) {
            StoreEntity(
                id = id,
                name = NameVO.from(name),
                type = type,
                ownerId = ownerId,
                address = AddressVO.from(address),
                timeRange = TimeRangeVO.from(timeRange),
                deleted = deleted
            )
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass = if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass = if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as StoreEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = id.hashCode()
}
