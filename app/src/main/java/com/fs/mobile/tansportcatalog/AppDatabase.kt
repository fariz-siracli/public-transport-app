package com.fs.mobile.tansportcatalog

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.fs.mobile.tansportcatalog.dao.CompanyDao
import com.fs.mobile.tansportcatalog.dao.PhoneDao
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.entity.CompanyTypeRelation
import com.fs.mobile.tansportcatalog.entity.Phone
import com.fs.mobile.tansportcatalog.entity.Type
import com.fs.mobile.tansportcatalog.utils.Constants

const val currentVersion = 8
const val newVersion = 9

@Database(entities = [Company::class, Type::class, CompanyTypeRelation::class, Phone::class], version = newVersion)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun phoneDao(): PhoneDao

    companion object {
        @JvmField
        val MIGRATION = MigrationRoom()
        private var INSTANCE: AppDatabase? = null
        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE =
                            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, Constants.DB_NAME)
                                .addMigrations(MIGRATION)
                                //                               .fallbackToDestructiveMigration()
                                .build()
                }
            }
            return INSTANCE
        }
    }
}

class MigrationRoom : Migration(currentVersion, newVersion) {
    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("ALTER TABLE company "
//                + " ADD COLUMN about TEXT");
    }
}