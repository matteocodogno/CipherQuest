package code.nebula.cipherquest.service.converter

import org.springframework.ai.tool.execution.ToolCallResultConverter
import java.lang.reflect.Type

class RemoveSurroundingQuotesConverter : ToolCallResultConverter {
    override fun convert(
        result: Any?,
        returnType: Type?,
    ): String = (result as String).removeSurrounding("\"", "\"")
}
