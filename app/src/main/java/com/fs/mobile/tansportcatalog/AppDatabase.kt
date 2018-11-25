package com.fs.mobile.tansportcatalog

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.utils.Constants

@Database(entities = [Company::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,  Constants.DB_NAME).build()
                }
            }
            return INSTANCE
        }
    }
}