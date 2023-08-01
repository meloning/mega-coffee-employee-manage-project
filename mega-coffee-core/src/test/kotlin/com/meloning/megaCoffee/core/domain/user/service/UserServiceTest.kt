package com.meloning.megaCoffee.core.domain.user.service

import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.PhoneNumber
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.domain.user.usecase.UserService
import com.meloning.megaCoffee.core.domain.user.usecase.command.CreateUserCommand
import com.meloning.megaCoffee.core.domain.user.usecase.command.UpdateUserCommand
import com.meloning.megaCoffee.core.event.EventSender
import com.meloning.megaCoffee.core.exception.AlreadyExistException
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

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: IUserRepository

    @Mock
    private lateinit var storeRepository: IStoreRepository

    @Mock
    private lateinit var educationRepository: IEducationRepository

    @Mock
    private lateinit var eventSender: EventSender

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    @DisplayName("유저 생성 테스트")
    fun createTest() {
        // given
        val mockEmail = "melon8372@gmail.com"
        val mockName = Name("메로닝")
        val mockAddress = Address("서울", "관악", "1234")
        val mockPhoneNumber = PhoneNumber("010-1234-1234")

        val command = CreateUserCommand(
            email = mockEmail,
            name = mockName,
            address = mockAddress,
            employeeType = EmployeeType.MANAGER,
            phoneNumber = mockPhoneNumber,
            workTimeType = WorkTimeType.WEEKEND,
            storeId = 1
        )

        val mockStore = Store(
            id = 1,
            name = Name("관악점"),
            type = StoreType.FRANCHISE,
            ownerId = 1,
            address = mockAddress,
            timeRange = TimeRange.DUMMY
        )

        val mockUser = User(
            id = 1,
            name = mockName,
            email = mockEmail,
            homeAddress = mockAddress,
            employeeType = EmployeeType.MANAGER,
            phoneNumber = mockPhoneNumber,
            workTimeType = WorkTimeType.WEEKEND,
            storeId = 1
        )

        whenever(userRepository.existsByNameAndEmail(mockName, mockEmail)).thenReturn(false)
        whenever(storeRepository.findNotDeletedById(1)).thenReturn(mockStore)
        whenever(userRepository.save(any())).thenReturn(mockUser)

        // when
        val (user, store) = userService.create(command)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                user.run {
                    assertThat(id).isEqualTo(1)
                    assertThat(name.value).isEqualTo("메로닝")
                    assertThat(email).isEqualTo("melon8372@gmail.com")
                    assertThat(homeAddress).isEqualTo(Address("서울", "관악", "1234"))
                    assertThat(deleted).isFalse
                    assertThat(phoneNumber.phone).isEqualTo("010-1234-1234")
                    assertThat(employeeType).isEqualTo(EmployeeType.MANAGER)
                    assertThat(workTimeType).isEqualTo(WorkTimeType.WEEKEND)
                    assertThat(createdAt).isNull()
                    assertThat(updatedAt).isNull()
                }
                store.run {
                    assertThat(id).isEqualTo(1)
                    assertThat(name.value).isEqualTo("관악점")
                    assertThat(type).isEqualTo(StoreType.FRANCHISE)
                    assertThat(ownerId).isEqualTo(1)
                    assertThat(address).isEqualTo(address)
                    assertThat(timeRange).isEqualTo(TimeRange.DUMMY)
                }
            }
        }
    }

    @Test
    @DisplayName("유저 생성시 이미 존재하는 경우")
    fun alreadyExistTest() {
        // given
        val mockEmail = "melon8372@gmail.com"
        val mockName = Name("메로닝")
        val mockAddress = Address("서울", "관악", "1234")
        val mockPhoneNumber = PhoneNumber("010-1234-1234")

        val command = CreateUserCommand(
            email = mockEmail,
            name = mockName,
            address = mockAddress,
            employeeType = EmployeeType.MANAGER,
            phoneNumber = mockPhoneNumber,
            workTimeType = WorkTimeType.WEEKEND,
            storeId = 1
        )

        whenever(userRepository.existsByNameAndEmail(mockName, mockEmail)).thenReturn(true)

        // then, return
        Assertions.assertThatThrownBy {
            userService.create(command)
        }.isInstanceOf(AlreadyExistException::class.java)
            .hasMessage("이미 존재하는 유저입니다.")
    }

    @Test
    @DisplayName("유저 변경 테스트")
    fun updateTest() {
        // given
        val mockEmail = "melon8372@gmail.com"
        val mockName = Name("메로닝")
        val mockAddress = Address("서울", "관악", "1234")
        val mockPhoneNumber = PhoneNumber("010-1234-1234")

        val command = UpdateUserCommand(
            address = Address("부산", "해운대", "123"),
            employeeType = EmployeeType.PART_TIME,
            phoneNumber = PhoneNumber("010-1111-2222"),
            workTimeType = WorkTimeType.WEEKDAY,
            storeId = 2
        )

        val mockUser = User(
            id = 1,
            name = mockName,
            email = mockEmail,
            homeAddress = mockAddress,
            employeeType = EmployeeType.MANAGER,
            phoneNumber = mockPhoneNumber,
            workTimeType = WorkTimeType.WEEKEND,
            storeId = 1
        )

        val mockStore = Store(
            id = 2,
            name = Name("관악점"),
            type = StoreType.FRANCHISE,
            ownerId = 1,
            address = mockAddress,
            timeRange = TimeRange.DUMMY
        )

        whenever(userRepository.findById(any())).thenReturn(mockUser)
        whenever(storeRepository.findNotDeletedById(any())).thenReturn(mockStore)

        // when
        val updatedUser = userService.update(1, command)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                updatedUser.run {
                    assertThat(homeAddress).isEqualTo(command.address)
                    assertThat(employeeType).isEqualTo(command.employeeType)
                    assertThat(phoneNumber).isEqualTo(command.phoneNumber)
                    assertThat(workTimeType).isEqualTo(command.workTimeType)
                    assertThat(storeId).isEqualTo(command.storeId)
                }
            }
        }
    }
}
