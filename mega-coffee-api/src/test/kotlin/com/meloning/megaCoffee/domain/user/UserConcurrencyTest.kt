package com.meloning.megaCoffee.domain.user

import com.meloning.megaCoffee.ApiTest
import com.meloning.megaCoffee.core.domain.common.Address
import com.meloning.megaCoffee.core.domain.common.Name
import com.meloning.megaCoffee.core.domain.common.TimeRange
import com.meloning.megaCoffee.core.domain.education.model.Education
import com.meloning.megaCoffee.core.domain.education.model.EducationAddress
import com.meloning.megaCoffee.core.domain.education.model.EducationAddresses
import com.meloning.megaCoffee.core.domain.education.repository.IEducationRepository
import com.meloning.megaCoffee.core.domain.store.model.Store
import com.meloning.megaCoffee.core.domain.store.model.StoreType
import com.meloning.megaCoffee.core.domain.store.repository.IStoreRepository
import com.meloning.megaCoffee.core.domain.user.model.EmployeeType
import com.meloning.megaCoffee.core.domain.user.model.User
import com.meloning.megaCoffee.core.domain.user.model.WorkTimeType
import com.meloning.megaCoffee.core.domain.user.repository.IUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserConcurrencyTest : ApiTest() {

    private lateinit var executorService: ExecutorService
    private lateinit var latch: CountDownLatch

    @Autowired
    private lateinit var storeRepository: IStoreRepository

    @Autowired
    private lateinit var userRepository: IUserRepository

    @Autowired
    private lateinit var educationRepository: IEducationRepository

    @BeforeEach
    fun init() {
        executorService = Executors.newFixedThreadPool(THREAD_SIZE)
        latch = CountDownLatch(THREAD_SIZE)

        val targetTypes = mutableListOf(EmployeeType.MANAGER, EmployeeType.PART_TIME)
        val education = educationRepository.save(
            Education(null, Name("다양한 커피 음료 및 레시피에 대해 학습하는 교육 프로그램입니다."), "커피 음료와 레시피 교육", targetTypes)
        )
        val educationAddresses = mutableListOf(
            EducationAddress(null, education, Address("부산", "해운대구 해운대로 111번길", "48100"), 1, 0, LocalDate.of(2023, 12, 31), TimeRange(LocalTime.of(19, 0), LocalTime.of(20, 0)))
        )
        educationRepository.update(education.apply { update(EducationAddresses(educationAddresses)) })

        val store = storeRepository.save(Store(null, Name("메가커피 서대문점"), StoreType.FRANCHISE, false))
        userRepository.save(User(null, Name("메로닝"), EmployeeType.PART_TIME, WorkTimeType.WEEKEND, store.id!!))
        storeRepository.update(store.apply { addEducation(education.id!!) })
    }

    @Test
    @DisplayName("교육 신청 동시성 테스트")
    fun test() {
        val userId = 1L
        val request = UserSteps.교육장소_등록()

        for (i in 0..THREAD_SIZE) {
            executorService.execute {
                UserSteps.교육장소_등록_요청(userId, request)
                latch.countDown()
            }
        }
        latch.await()

        val detailResponse = UserSteps.상세_요청(userId)
        SoftAssertions.assertSoftly {
            it.run {
                assertThat(detailResponse.jsonPath().getList<Any>("educations").size).isEqualTo(1)
            }
        }
    }

    companion object {
        const val THREAD_SIZE = 2
    }
}
