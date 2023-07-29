package com.meloning.megaCoffee.infra.database.mysql.domain.user.entity

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.infra.database.mysql.domain.common.AddressVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.PhoneNumberVO

object UserEntityFixture {
    internal fun generateTestUserEntities(): List<UserEntity> = listOf(
        UserEntity(1, "melon8372@gmail.com", NameVO("메로닝"), AddressVO("서울", "관악", "111"), EmployeeType.OWNER, PhoneNumberVO("010-2341-2345"), WorkTimeType.WEEKDAY, 1, false),
        UserEntity(2, "wnstn8372@gmail.com", NameVO("장준수"), AddressVO("서울", "강남", "112"), EmployeeType.SUPER_MANAGER, PhoneNumberVO("010-1111-2222"), WorkTimeType.WEEKEND, 1, false),
        UserEntity(3, "melon8372@naver.com", NameVO("아낙네"), AddressVO("서울", "강북", "113"), EmployeeType.MANAGER, PhoneNumberVO("010-3333-4444"), WorkTimeType.WEEKDAY, 1, false),
        UserEntity(4, "melon8372@daum.com", NameVO("어디가"), AddressVO("서울", "노량진", "114"), EmployeeType.PART_TIME, PhoneNumberVO("010-5555-6666"), WorkTimeType.WEEKEND, 1, false),
        UserEntity(5, "melon8372@kakao.com", NameVO("왜안와"), AddressVO("서울", "서초", "1231"), EmployeeType.OWNER, PhoneNumberVO("010-7777-8888"), WorkTimeType.WEEKDAY, 2, false),
        UserEntity(6, "melon8372@asdf.com", NameVO("미아좀"), AddressVO("서울", "역삼", "1232"), EmployeeType.SUPER_MANAGER, PhoneNumberVO("010-9999-1111"), WorkTimeType.WEEKEND, 2, false),
        UserEntity(7, "melon8372@qwer.com", NameVO("갱좀와"), AddressVO("서울", "삼성", "1233"), EmployeeType.MANAGER, PhoneNumberVO("010-1234-1234"), WorkTimeType.WEEKEND, 2, false),
        UserEntity(8, "melon8372@zxcv.com", NameVO("누칼협"), AddressVO("서울", "상수", "1234"), EmployeeType.PART_TIME, PhoneNumberVO("010-1234-5678"), WorkTimeType.WEEKDAY, 2, false),
        UserEntity(9, "melon8372@rtyu.com", NameVO("넌뭔데"), AddressVO("서울", "사당", "1235"), EmployeeType.MANAGER, PhoneNumberVO("010-9876-5432"), WorkTimeType.WEEKEND, 1, false),
        UserEntity(10, "melon8372@fghj.com", NameVO("알았다"), AddressVO("서울", "교대", "1236"), EmployeeType.PART_TIME, PhoneNumberVO("010-1234-9876"), WorkTimeType.WEEKEND, 1, false),
    )
}
