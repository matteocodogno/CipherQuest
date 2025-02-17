package code.nebula.cipherquest.components

import code.nebula.cipherquest.models.UserStatus
import code.nebula.cipherquest.models.dto.Source
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class MessageContext :
    MutableMap<String, Any> by mutableMapOf(
        "status" to UserStatus.IN_PROGRESS,
        "isLevelUp" to false,
        "sources" to emptyList<Source>(),
    ) {
    var status: UserStatus
        get() = this["status"] as UserStatus
        set(value) {
            this["status"] = value
        }

    var isLevelUp: Boolean
        get() = this["isLevelUp"] as Boolean
        set(value) {
            this["isLevelUp"] = value
        }

    var sources: List<Source>
        get() =
            if (this["sources"] is List<*>) {
                @Suppress("UNCHECKED_CAST")
                this["sources"] as List<Source>
            } else {
                emptyList()
            }
        set(value) {
            this["sources"] = value
        }
}
