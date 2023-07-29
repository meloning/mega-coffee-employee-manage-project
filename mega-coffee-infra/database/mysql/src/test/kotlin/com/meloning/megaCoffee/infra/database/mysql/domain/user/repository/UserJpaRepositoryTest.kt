package com.meloning.megaCoffee.infra.database.mysql.domain.user.repository

import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.usecase.command.ScrollUserCommand
import com.meloning.megaCoffee.infra.database.mysql.config.AuditConfig
import com.meloning.megaCoffee.infra.database.mysql.config.QuerydslConfig
import com.meloning.megaCoffee.infra.database.mysql.config.TestConfig
import com.meloning.megaCoffee.infra.database.mysql.domain.common.AddressVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.NameVO
import com.meloning.megaCoffee.infra.database.mysql.domain.common.PhoneNumberVO
import com.meloning.megaCoffee.infra.database.mysql.domain.store.entity.StoreEntityFixture
import com.meloning.megaCoffee.infra.database.mysql.domain.store.repository.StoreJpaRepository
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.UserEntity
import com.meloning.megaCoffee.infra.database.mysql.domain.user.entity.UserEntityFixture
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        TestConfig::class,
        QuerydslConfig::class,
        AuditConfig::class
    ]
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserJpaRepositoryTest {

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    private lateinit var storeJpaRepository: StoreJpaRepository

    @BeforeAll
    internal fun init() {
        userJpaRepository.saveAll(UserEntityFixture.generateTestUserEntities())
        storeJpaRepository.saveAll(StoreEntityFixture.generateTestStoreEntities())
    }

    @Test
    @DisplayName("Audit 생성 테스트")
    fun auditCreateTest() {
        // given
        val userEntity = UserEntity(
            id = null,
            email = "melon8372@gmail.com",
            name = NameVO("메로닝"),
            address = AddressVO("서울특별시", "관악구 신림동", "12345"),
            employeeType = EmployeeType.OWNER,
            phoneNumber = PhoneNumberVO("010-1234-1234"),
            workTimeType = WorkTimeType.WEEKDAY,
            storeId = 1,
            deleted = false
        )

        // when
        val user = userJpaRepository.save(userEntity)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(user.id).isNotNull
                assertThat(user.createdAt).isNotNull
                assertThat(user.updatedAt).isNotNull
            }
        }
    }

    @ParameterizedTest
    @DisplayName("유저 스크롤 조회 테스트")
    @CsvSource(
        "0, , TRUE, 1",
        "1, 5, FALSE, 6",
    )
    fun scrollTest(page: Int, userId: Long?, expectedHasNext: Boolean, expectedUserId: Long) {
        // given
        val command = ScrollUserCommand(userId, null, null, null, null)
        val pageRequest = PageRequest.of(page, 5)

        // when
        val (content, hasNext) = userJpaRepository.scroll(command, pageRequest.pageNumber, pageRequest.pageSize)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(hasNext).isEqualTo(expectedHasNext)
                assertThat(content.size).isEqualTo(5)
                assertThat(content.first().id).isEqualTo(expectedUserId)
            }
        }
    }

    @ParameterizedTest
    @DisplayName("유저 스크롤 필터링 테스트")
    @CsvSource(
        "OWNER, , , 2",
        " , WEEKDAY, , 4",
        " , , 2, 4",
        "PART_TIME, WEEKEND, , 2",
        " , WEEKDAY, 2, 2",
        "MANAGER, WEEKEND, 1, 1",
    )
    fun scrollFilterTest(employeeType: EmployeeType?, workTimeType: WorkTimeType?, storeId: Long?, resultSize: Int) {
        // given
        val command = ScrollUserCommand(null, null, employeeType, workTimeType, storeId)
        val pageRequest = PageRequest.of(0, 5)

        // when
        val (content, hasNext) = userJpaRepository.scroll(command, pageRequest.pageNumber, pageRequest.pageSize)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(hasNext).isFalse
                assertThat(content.size).isEqualTo(resultSize)
            }
        }
    }

    @ParameterizedTest
    @DisplayName("유저 스크롤 이름 검색 테스트")
    @CsvSource(
        "메로닝, 1, FALSE",
        "장준수, 1, FALSE",
        " , 5, TRUE",
        "좀, 2, FALSE",
        "아, 2, FALSE",
        "뉘신지, 0, FALSE",
    )
    fun scrollSearchNameTest(keyword: String?, resultSize: Int, expectedHasNext: Boolean) {
        // given
        val command = ScrollUserCommand(null, keyword, null, null, null)
        val pageRequest = PageRequest.of(0, 5)

        // when
        val (content, hasNext) = userJpaRepository.scroll(command, pageRequest.pageNumber, pageRequest.pageSize)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(hasNext).isEqualTo(expectedHasNext)
                assertThat(content.size).isEqualTo(resultSize)
            }
        }
    }
}
