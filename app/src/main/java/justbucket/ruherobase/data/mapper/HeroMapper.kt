package justbucket.ruherobase.data.mapper

import justbucket.ruherobase.data.model.HeroDescriptionEntity
import justbucket.ruherobase.data.model.HeroEntity
import justbucket.ruherobase.data.model.HeroOccupationEntity
import justbucket.ruherobase.domain.model.Hero

fun Hero.mapToData() =
    Triple(
        HeroEntity(id, name, photoUrl, mentionNumber),
        HeroDescriptionEntity(null, id, description),
        HeroOccupationEntity(null, id, occupation)
    )

fun Triple<HeroEntity, HeroOccupationEntity, HeroDescriptionEntity>.mapToDomain() =
    Hero(first.heroId!!, first.heroName, first.mentionedNumber, third.description, second.occupation, first.heroPhotoUrl)

