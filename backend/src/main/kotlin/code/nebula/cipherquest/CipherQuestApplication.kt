package code.nebula.cipherquest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
@ConfigurationPropertiesScan("code.nebula.cipherquest.configuration.properties")
class CipherQuestApplication

fun main(args: Array<String>) {
    runApplication<CipherQuestApplication>(args = args)
}
