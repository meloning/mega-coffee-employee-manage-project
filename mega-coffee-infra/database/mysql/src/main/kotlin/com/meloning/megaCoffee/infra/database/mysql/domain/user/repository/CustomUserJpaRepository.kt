package com.meloning.megaCoffee.infra.database.mysql.domain.user.repository

import com.meloning.megaCoffee.core.domain.user.usecase.command.ScrollUserCommand
import com.meloning.megaCoffee.core.util.InfiniteScrollType
import com.meloning.megaCoffee.infra.database.mysql.domain.user.repository.dto.UserShortRow

interface CustomUserJpaRepository {
    fun scroll(command: ScrollUserCommand, page: Int, size: Int): InfiniteScrollType<UserShortRow>
}
