package code.nebula.cipherquest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("code.nebula.cipherquest.configuration.properties")
class CipherQuestApplication

fun main(args: Array<String>) {
    runApplication<CipherQuestApplication>(args = args)
}
