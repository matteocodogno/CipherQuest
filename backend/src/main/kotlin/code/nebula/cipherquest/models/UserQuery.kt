package code.nebula.cipherquest.models

import code.nebula.cipherquest.repository.entities.UserLevel

data class UserQuery(
    val pair: Pair<UserLevel, String>,
) {
    val user: UserLevel by pair::first
    val message: String by pair::second
}
