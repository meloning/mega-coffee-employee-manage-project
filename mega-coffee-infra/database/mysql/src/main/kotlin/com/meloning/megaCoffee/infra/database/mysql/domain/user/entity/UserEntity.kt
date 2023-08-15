package com.meloning.megaCoffee.infra.database.mysql.domain.user.entity

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.infra.database.mysql.domain.common.AddressVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.BaseTimeEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.PhoneNumberVO
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.SQLDelete
import org.hibernate.proxy.HibernateProxy
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update user set is_deleted = true where id = ?")
class UserEntity : BaseTimeEntity {

    constructor(id: Long?, email: String, name: NameVO, address: AddressVO, employeeType: EmployeeType, phoneNumber: PhoneNumberVO, workTimeType: WorkTimeType, storeId: Long, deleted: Boolean) : super() {
        this.id = id
        this.email = email
        this.name = name
        this.address = address
        this.employeeType = employeeType
        this.phoneNumber = phoneNumber
        this.workTimeType = workTimeType
        this.storeId = storeId
        this.deleted = deleted
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    val email: String

    @Embedded
    val name: NameVO

    @Embedded
    var address: AddressVO
        protected set

    @Enumerated(value = EnumType.STRING)
    var employeeType: EmployeeType
        protected set

    @Embedded
    var phoneNumber: PhoneNumberVO
        protected set

    @Enumerated(value = EnumType.STRING)
    var workTimeType: WorkTimeType
        protected set

    var storeId: Long
        protected set

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault(value = "0")
    var deleted: Boolean = false
        protected set

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var educationAddressRelations: MutableSet<UserEducationAddressRelationEntity> = mutableSetOf()
        protected set

    fun update(educationAddresses: MutableSet<UserEducationAddressRelationEntity>) {
        this.educationAddressRelations = educationAddresses
    }

    fun toModel(): User = User(
        id = id,
        email = email,
        name = name.toModel(),
        homeAddress = address.toModel(),
        employeeType = employeeType,
        phoneNumber = phoneNumber.toModel(),
        workTimeType = workTimeType,
        storeId = storeId,
        deleted = deleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        @JvmStatic
        fun from(model: User) = with(model) {
            UserEntity(
                id = id,
                email = email,
                name = NameVO.from(name),
                address = AddressVO.from(homeAddress),
                employeeType = employeeType,
                phoneNumber = PhoneNumberVO.from(phoneNumber),
                workTimeType = workTimeType,
                storeId = storeId,
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
        other as UserEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int = id.hashCode()
}
