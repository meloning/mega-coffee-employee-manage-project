package com.meloning.megaCoffee.infra.database.mysql.domain.store.entity

import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.infra.database.mysql.domain.common.AddressVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.TimeRangeVO
import java.time.LocalTime

object StoreEntityFixture {
    internal fun generateTestStoreEntities(): List<StoreEntity> = listOf(
        StoreEntity(1, NameVO("서대문점"), StoreType.FRANCHISE, 1, AddressVO("서울", "서대문", "123"), TimeRangeVO(LocalTime.of(8, 0), LocalTime.of(23, 0)), false),
        StoreEntity(2, NameVO("신림점"), StoreType.COMPANY_OWNED, 5, AddressVO("서울", "신림", "123"), TimeRangeVO(LocalTime.of(10, 0), LocalTime.of(21, 0)), false),
    )
}
