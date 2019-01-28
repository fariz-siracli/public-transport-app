package com.fs.mobile.tansportcatalog.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.fs.mobile.tansportcatalog.dao.CompanyDao
import com.fs.mobile.tansportcatalog.dao.PhoneDao
import com.fs.mobile.tansportcatalog.db.App.DATABASE_VERSION
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.entity.CompanyTypeRelation
import com.fs.mobile.tansportcatalog.entity.Phone
import com.fs.mobile.tansportcatalog.entity.Type
import com.fs.mobile.tansportcatalog.utils.Constants
import com.fs.mobile.tansportcatalog.utils.Utils

//const val currentVersion = 8
//const val newVersion = 9
object App {
    const val DATABASE_VERSION = 11
    fun migrate(database: SupportSQLiteDatabase) {
        Utils.log(Constants.CREATE_SCRIPT)
        database.execSQL(Constants.CREATE_SCRIPT)

        Utils.log(Constants.INSERT_SCRIPT)
        database.execSQL(Constants.INSERT_SCRIPT)
        // Remove the old table
//        database.execSQL(Constants.DROP_TABLE)
        // Change the table name to the correct one
//        database.execSQL(Constants.RENAME_SCRIPT)
    }
}


@Database(
    entities = [Company::class, Type::class, CompanyTypeRelation::class, Phone::class],
    version = DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun phoneDao(): PhoneDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE =
                            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, Constants.DB_NAME)
                                .addMigrations(
                                    MigrationRoom_1_2(),
                                    MigrationRoom_2_3(),
                                    MigrationRoom_3_4(),
                                    MigrationRoom_4_5(),
                                    MigrationRoom_5_6(),
                                    MigrationRoom_6_7(),
                                    MigrationRoom_7_8(),
                                    MigrationRoom_8_9(),
                                    MigrationRoom_9_10(),
                                    MigrationRoom_9_10(),
                                    MigrationRoom_10_11(),
                                    MigrationRoom_11_12()
                                ).build()
                }
            }
            return INSTANCE
        }
    }
}

class MigrationRoom_1_2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_1_2")
    }
}

class MigrationRoom_2_3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_2_3")
    }
}

class MigrationRoom_3_4 : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_3_4")
    }
}

class MigrationRoom_4_5 : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_4_5")
    }
}

class MigrationRoom_5_6 : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_5_6")
    }
}

class MigrationRoom_6_7 : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_6_7")
    }
}

class MigrationRoom_7_8 : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_7_8")
    }
}

class MigrationRoom_8_9 : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_8_9")
    }
}

class MigrationRoom_9_10 : Migration(9, 10) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_9_10")
    }
}

class MigrationRoom_10_11 : Migration(10, 11) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_10_11")
    }
}

class MigrationRoom_11_12 : Migration(11, 12) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Utils.log("MigrationRoom_11_12")
    }
}
