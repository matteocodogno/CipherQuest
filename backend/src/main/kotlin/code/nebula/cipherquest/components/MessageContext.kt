package code.nebula.cipherquest.components

import code.nebula.cipherquest.models.UserStatus
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class MessageContext {
    val context: MutableMap<String, Any> =
        mutableMapOf(
            "status" to UserStatus.IN_PROGRESS,
            "isLevelUp" to false,
            "sources" to emptyList<String>(),
        )
}
