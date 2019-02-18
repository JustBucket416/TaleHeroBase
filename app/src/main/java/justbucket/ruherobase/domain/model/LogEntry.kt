package justbucket.ruherobase.domain.model

import java.util.*

data class LogEntry(
    val user: User,
    val date: Date,
    val accessType: AccessType,
    val hero: Hero
)