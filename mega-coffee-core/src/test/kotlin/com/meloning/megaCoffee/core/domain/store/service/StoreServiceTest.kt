package com.meloning.megaCoffee.core.domain.store.service

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.store.usecase.StoreService
import com.meloning.megaCoffee.core.domain.store.usecase.command.CreateStoreCommand
import com.meloning.megaCoffee.core.domain.store.usecase.command.UpdateStoreCommand
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class StoreServiceTest {

    @Mock
    private lateinit var storeRepository: IStoreRepository

    @Mock
    private lateinit var userRepository: IUserRepository

    @Mock
    private lateinit var educationRepository: IEducationRepository

    @InjectMocks
    private lateinit var storeService: StoreService

    @Test
    @DisplayName("유저 생성 테스트")
    fun createTest() {
        // given
        val mockName = Name("관악점")
        val mockAddress = Address("서울", "관악구 신림동", "1234")
        val mockTimeRange = TimeRange(LocalTime.of(8, 0), LocalTime.of(20, 0))

        val command = CreateStoreCommand(
            name = mockName,
            type = StoreType.FRANCHISE,
            ownerId = 1,
            address = mockAddress,
            timeRange = mockTimeRange
        )

        val mockUser = User(
            id = 1,
            name = Name("메로닝"),
            email = "melon8372@gmail.com",
            homeAddress = mockAddress,
            employeeType = EmployeeType.MANAGER,
            phoneNumber = PhoneNumber("01012341234"),
            workTimeType = WorkTimeType.WEEKEND,
            storeId = 1
        )

        val mockStore = Store(
            id = 1,
            name = mockName,
            type = StoreType.FRANCHISE,
            ownerId = 1,
            address = mockAddress,
            timeRange = mockTimeRange
        )

        whenever(storeRepository.existsByName(mockName)).thenReturn(false)
        whenever(userRepository.findById(any())).thenReturn(mockUser)
        whenever(storeRepository.save(any())).thenReturn(mockStore)

        // when
        val (store, _) = storeService.create(command)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                store.run {
                    assertThat(id).isEqualTo(1)
                    assertThat(name.value).isEqualTo(command.name.value)
                    assertThat(type).isEqualTo(command.type)
                    assertThat(ownerId).isEqualTo(command.ownerId)
                    assertThat(address).isEqualTo(command.address)
                    assertThat(timeRange).isEqualTo(command.timeRange)
                    assertThat(createdAt).isNull()
                    assertThat(updatedAt).isNull()
                }
            }
        }
    }

    @Test
    @DisplayName("매장 생성시 이미 존재하는 경우")
    fun alreadyExistTest() {
        // given
        val mockName = Name("관악점")
        val mockAddress = Address("서울", "관악구 신림동", "1234")
        val mockTimeRange = TimeRange(LocalTime.of(8, 0), LocalTime.of(20, 0))

        val command = CreateStoreCommand(
            name = mockName,
            type = StoreType.FRANCHISE,
            ownerId = 1,
            address = mockAddress,
            timeRange = mockTimeRange
        )

        whenever(storeRepository.existsByName(mockName)).thenReturn(true)

        // when, then
        Assertions.assertThatThrownBy {
            storeService.create(command)
        }.isInstanceOf(RuntimeException::class.java)
            .hasMessage("이미 존재하는 매장입니다.")
    }

    @Test
    @DisplayName("매장 변경 테스트")
    fun updateTest() {
        // given
        val mockName = Name("관악점")
        val mockAddress = Address("서울", "관악구 신림동", "1234")
        val mockTimeRange = TimeRange(LocalTime.of(8, 0), LocalTime.of(20, 0))

        val mockStore = Store(
            id = 1,
            name = mockName,
            type = StoreType.FRANCHISE,
            ownerId = 1,
            address = mockAddress,
            timeRange = mockTimeRange
        )

        val command = UpdateStoreCommand(
            type = StoreType.COMPANY_OWNED,
            ownerId = 2,
            address = Address("서울", "강남구 삼성동", "1234"),
            timeRange = TimeRange(LocalTime.of(11, 0), LocalTime.of(21, 0))
        )

        val mockUser = User(
            id = 2,
            name = Name("메로닝"),
            email = "melon8372@gmail.com",
            homeAddress = mockAddress,
            employeeType = EmployeeType.MANAGER,
            phoneNumber = PhoneNumber("01012341234"),
            workTimeType = WorkTimeType.WEEKEND,
            storeId = 1
        )

        whenever(storeRepository.findById(any())).thenReturn(mockStore)
        whenever(userRepository.findById(any())).thenReturn(mockUser)

        // when
        val updatedStore = storeService.update(1, command)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                updatedStore.run {
                    assertThat(type).isEqualTo(command.type)
                    assertThat(ownerId).isEqualTo(command.ownerId)
                    assertThat(address).isEqualTo(command.address)
                    assertThat(timeRange).isEqualTo(command.timeRange)
                }
            }
        }
    }
}
