package com.meloning.megaCoffee.core.api

import com.meloning.megaCoffee.infra.api.dto.EducationPlaceSimpleResponse
import com.meloning.megaCoffee.infra.api.dto.ParticipantResponse
import java.time.LocalDate

interface EmployeeManageExternalApi {
    /**
     * 특정 날짜의 교육장소 리스트 API
     * @param LocalDate
     * @return List<EducationPlace>
     */
    fun getEducationPlacesByDate(date: LocalDate): List<EducationPlaceSimpleResponse>

    /**
     * 특정 교육 장소의 현재 참여자 리스트 API
     * @param Long
     * @return List<User>
     */
    fun getParticipantsByEducationPlace(educationPlaceId: Long): List<ParticipantResponse>
}
