package com.meloning.megaCoffee.core.domain.education.service

import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationPlaces
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.education.usecase.EducationService
import com.meloning.megaCoffee.core.domain.education.usecase.command.CreateEducationCommand
import com.meloning.megaCoffee.core.domain.relation.repository.IStoreEducationRelationRepository
import com.meloning.megaCoffee.core.domain.relation.repository.IUserEducationPlaceRelationRepository
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import com.meloning.megaCoffee.core.exception.AlreadyExistException
import com.meloning.megaCoffee.core.infra.event.EventSender
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
class EducationServiceTest {

    @Mock
    private lateinit var educationRepository: IEducationRepository

    @Mock
    private lateinit var userRepository: IUserRepository

    @Mock
    private lateinit var storeRepository: IStoreRepository

    @Mock
    private lateinit var storeEducationRelationRepository: IStoreEducationRelationRepository

    @Mock
    private lateinit var userEducationRelationRepository: IUserEducationPlaceRelationRepository

    @Mock
    private lateinit var eventSender: EventSender

    @InjectMocks
    private lateinit var educationService: EducationService

    @Test
    @DisplayName("교육 생성 테스트")
    fun createTest() {
        // given
        val mockName = Name("신입 매니저, 알바 OJT")
        val mockContent = "신입 매니저와 알바생을 위한 OJT 프로그램입니다."
        val mockTargetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME)

        val command = CreateEducationCommand(
            name = mockName,
            content = mockContent,
            targetTypes = mockTargetTypes
        )

        val mockEducation = Education(
            id = 1,
            name = mockName,
            content = mockContent,
            targetTypes = mockTargetTypes,
            educationPlaces = EducationPlaces(mutableListOf())
        )

        whenever(educationRepository.existsByName(mockName)).thenReturn(false)
        whenever(educationRepository.save(any())).thenReturn(mockEducation)

        // when
        val education = educationService.create(command)

        // then
        SoftAssertions.assertSoftly {
            it.run {
                education.run {
                    assertThat(name).isEqualTo(command.name)
                    assertThat(content).isEqualTo(command.content)
                    assertThat(targetTypes).containsExactly(*command.targetTypes.toTypedArray())
                }
            }
        }
    }

    @Test
    @DisplayName("교육 프로그램이 이미 존재하는 경우 테스트")
    fun alreadyExistTest() {
        // given
        val mockName = Name("신입 매니저, 알바 OJT")
        val mockContent = "신입 매니저와 알바생을 위한 OJT 프로그램입니다."
        val mockTargetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME)

        val command = CreateEducationCommand(
            name = mockName,
            content = mockContent,
            targetTypes = mockTargetTypes
        )

        whenever(educationRepository.existsByName(mockName)).thenReturn(true)

        // when, then
        Assertions.assertThatThrownBy {
            educationService.create(command)
        }.isInstanceOf(AlreadyExistException::class.java)
            .hasMessage("이미 존재하는 교육프로그램입니다.")
    }
}
