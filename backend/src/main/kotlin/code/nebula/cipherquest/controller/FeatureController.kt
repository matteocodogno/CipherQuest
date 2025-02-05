package code.nebula.cipherquest.controller

import code.nebula.cipherquest.configuration.properties.FeatureFlags
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/features")
class FeatureController(
    private val featureFlags: FeatureFlags,
) {
    @GetMapping
    fun getFeatureFlags(): ResponseEntity<FeatureFlags> = ResponseEntity.ok(featureFlags)
}
