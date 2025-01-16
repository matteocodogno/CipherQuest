package code.nebula.cipherquest.controller.private

import code.nebula.cipherquest.service.UserLevelService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/private/user")
class PrivateUserLevelController(
    private val userLevelService: UserLevelService,
) {
    @GetMapping
    fun getUsers() = userLevelService.findAll()

    @GetMapping("/{username}")
    fun findUserByUsername(
        @PathVariable username: String,
    ) = userLevelService.findUserByUsername(username)
}
