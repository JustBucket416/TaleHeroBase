package justbucket.ruherobase.domain.model

data class Hero(
    val id: Long,
    val name: String,
    val mentionNumber: Int,
    val description: String,
    val occupation: String,
    val photoUrl: String
)