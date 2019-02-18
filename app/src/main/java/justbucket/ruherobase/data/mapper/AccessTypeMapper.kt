package justbucket.ruherobase.data.mapper

import justbucket.ruherobase.data.model.AccessTypeEntity
import justbucket.ruherobase.domain.model.AccessType
import java.util.*

fun AccessType.mapToData() = AccessTypeEntity(id, name)

fun mapAccessLongToDomain(long: Long) = when (long) {
    0L -> AccessType.COPY
    1L -> AccessType.READ
    2L -> AccessType.UPDATE
    3L -> AccessType.DELETE
    else -> throw IllegalArgumentException("unknown id")
}

fun List<AccessTypeEntity>.mapToDomain() = map { mapAccessLongToDomain(it.accessId) }.toSet() as EnumSet<AccessType>

fun EnumSet<AccessType>.mapToData() = map { it.mapToData() }.toList()