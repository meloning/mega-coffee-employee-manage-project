package com.meloning.megaCoffee.core.domain.user.model

enum class EmployeeType(val value: String) {
    OWNER("사장"),
    SUPER_MANAGER("점장"),
    MANAGER("매니저"),
    PART_TIME("알바");
}
