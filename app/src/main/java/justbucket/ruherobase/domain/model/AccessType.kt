package justbucket.ruherobase.domain.model

enum class AccessType(val id: Long) {
    CREATE(0),
    READ(1),
    UPDATE(2),
    DELETE(4)
}