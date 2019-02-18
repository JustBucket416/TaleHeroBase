package justbucket.ruherobase.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import justbucket.ruherobase.data.dao.*
import justbucket.ruherobase.data.model.*

@Database(
    entities = [AccessTypeEntity::class, HeroDescriptionEntity::class, HeroEntity::class, HeroOccupationEntity::class,
        LogEntity::class, RoleEntity::class, UserAccessLinkEntity::class, UserEntity::class, UserTypeEntity::class],
    version = 1
)
abstract class HeroDatabase : RoomDatabase() {

    abstract fun getAccessTypeDao(): AccessTypeDao

    abstract fun getDescriptionDao(): DescriptionDao

    abstract fun getHeroDao(): HeroDao

    abstract fun getLogDao(): LogDao

    abstract fun getOccupationDao(): OccupationDao

    abstract fun getRoleDao(): RoleDao

    abstract fun getUserDao(): UserDao

    abstract fun getUserTypeDao(): UserTypeDao

    abstract fun getLinkDao(): UserAccessLinkDao

    companion object {
        @Volatile
        private var INSTANCE: HeroDatabase? = null

        fun getDatabase(context: Context) =
            INSTANCE ?: synchronized(HeroDatabase::class.java) {
                INSTANCE ?: Room.databaseBuilder(context, HeroDatabase::class.java, "heroes.db")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            db.execSQL("INSERT OR IGNORE INTO AccessTypeEntity VALUES(0, \"create\"), (1, \"read\"), (2, \"update\"), (3, \"delete \") ")
                            db.execSQL("INSERT OT IGNORE INTO UserTypeEntity VALUES(0, \"Admin\"), (1, \"User\"), (2, \"SuperUser\"), (3, \"Moderator\")")
                            db.execSQL("INSERT OR IGNORE INTO UserAccessLinkEntity VALUES(0, 0), (0, 1), (0, 2), (0, 3), (1, 1), (2, 0), (2, 1), (3, 3), (3, 3)")
                        }
                    })
                    .build()
            }

    }
}