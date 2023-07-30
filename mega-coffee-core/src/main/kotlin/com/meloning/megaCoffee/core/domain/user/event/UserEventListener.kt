package com.meloning.megaCoffee.core.domain.user.event

import com.meloning.megaCoffee.core.event.EventSender
import com.meloning.megaCoffee.core.event.EventType
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserEventListener(
    private val eventSender: EventSender
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun applyEducationAddressListener(eventModel: AppliedUserEducationAddressEvent) {
        eventSender.send(
            EventType.EMAIL,
            with(eventModel) {
                mapOf(
                    "email" to email,
                    "username" to username,
                    "educationName" to educationName,
                    "educationAddress" to educationAddress,
                    "date" to date,
                    "time" to time,
                    "type" to "complete_user_education"
                )
            }
        )
    }
}