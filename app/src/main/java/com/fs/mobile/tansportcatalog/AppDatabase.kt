package com.fs.mobile.tansportcatalog

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.fs.mobile.tansportcatalog.dao.CompanyDao
import com.fs.mobile.tansportcatalog.dao.PhoneDao
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.entity.CompanyTypeRelation
import com.fs.mobile.tansportcatalog.entity.Phone
import com.fs.mobile.tansportcatalog.entity.Type
import com.fs.mobile.tansportcatalog.utils.Constants

@Database(entities = [Company::class, Type::class, CompanyTypeRelation::class, Phone::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun phoneDao(): PhoneDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, Constants.DB_NAME).build()
                }
            }
            return INSTANCE
        }
    }
}