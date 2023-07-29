package com.meloning.megaCoffee.core.domain.store.model

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.exception.NotRegisterException
import java.time.Instant

data class Store(
    val id: Long? = null,
    val name: Name,
    var type: StoreType,
    var ownerId: Long?,
    var address: Address,
    var timeRange: TimeRange,
    // 교육 프로그램 정보들
    var educations: MutableList<StoreEducationRelation> = mutableListOf(),
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null
) {
    constructor(id: Long?, name: Name, type: StoreType) :
        this(id, name, type, null, Address.DUMMY, TimeRange.DUMMY)

    fun update(
        type: StoreType? = null,
        ownerId: Long? = null,
        address: Address? = null,
        timeRange: TimeRange? = null
    ) {
        type?.let { this.type = it }
        this.ownerId = ownerId
        address?.let { this.address = it }
        timeRange?.let { this.timeRange = it }
    }

    fun addEducation(educationId: Long) {
        educations.add(StoreEducationRelation(store = this, educationId = educationId))
    }

    fun removeEducation(educationId: Long) {
        educations.removeIf { it.educationId == educationId }
    }

    fun validateEligibility(educationId: Long, educationName: String) {
        val educationIds = educations.map { it.educationId }
        val storeName = name.value

        if (!educationIds.contains(educationId)) {
            throw NotRegisterException("$storeName 매장의 직원은 $educationName 교육 프로그램을 들을 수 없습니다.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Store

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: javaClass.hashCode()
    }
}
