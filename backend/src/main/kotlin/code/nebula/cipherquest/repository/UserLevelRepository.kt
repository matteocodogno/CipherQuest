package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserLevelRepository : ListCrudRepository<UserLevel, String>
