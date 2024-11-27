package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.requests.CreateUserLevelRequest
import code.nebula.cipherquest.service.UserLevelService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserLevelController(
    private val userLevelService: UserLevelService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(
        @RequestBody request: CreateUserLevelRequest,
    ) = userLevelService.createUserLevel(request)
}
