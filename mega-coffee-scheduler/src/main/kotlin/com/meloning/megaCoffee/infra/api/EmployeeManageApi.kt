package com.meloning.megaCoffee.infra.api

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class EmployeeManageApi {

    /**
     * 특정 날짜의 교육장소 리스트 API
     * @param LocalDate
     * @return List<EducationPlace>
     */
    fun getEducationPlacesByDate(date: LocalDate): List<Void> {
        TODO("Not yet implemented")
    }

    /**
     * 특정 교육 장소의 현재 참여자 리스트 API
     * @param Long
     * @return List<User>
     */
    fun getParticipantsByEducationPlace(educationPlaceId: Long): List<Void> {
        TODO("Not yet implemented")
    }
}
