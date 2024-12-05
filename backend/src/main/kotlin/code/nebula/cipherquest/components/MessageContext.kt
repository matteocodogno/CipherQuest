package code.nebula.cipherquest.components

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class MessageContext {
    val context: MutableMap<String, Any> = mutableMapOf()
}

