package com.fs.mobile.tansportcatalog.db

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.entity.Phone
import com.fs.mobile.tansportcatalog.utils.Constants
import com.fs.mobile.tansportcatalog.utils.Constants.Companion.DATABASE_VERSION
import com.fs.mobile.tansportcatalog.utils.Utils
import java.io.File
import java.io.FileOutputStream

class DbHelper(val context: Context) : SQLiteOpenHelper(context, Constants.DB_NAME, null, DATABASE_VERSION) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        "${context.packageName}.database_versions",
        Context.MODE_PRIVATE
    )

    private fun installedDatabaseIsOutdated(): Boolean {
        return preferences.getInt(Constants.DB_NAME, 0) < DATABASE_VERSION
    }

    private fun writeDatabaseVersionInPreferences() {
        preferences.edit().apply {
            putInt(Constants.DB_NAME, DATABASE_VERSION)
            apply()
        }
    }

    private fun installDatabaseFromAssets() {
        val inputStream = context.assets.open("$Constants.DB_NAME")

        try {
            val outputFile = File(context.getDatabasePath(Constants.DB_NAME).path)
            val outputStream = FileOutputStream(outputFile)

            inputStream.copyTo(outputStream)
            inputStream.close()

            outputStream.flush()
            outputStream.close()
        } catch (exception: Throwable) {
            throw RuntimeException("The $Constants.DB_NAME database couldn't be installed.", exception)
        }
    }

    @Synchronized
    private fun installOrUpdateIfNecessary() {
        if (installedDatabaseIsOutdated()) {
            context.deleteDatabase(Constants.DB_NAME)
//            installDatabaseFromAssets()
            Utils.copyDataBaseToApp(context, Constants.DB_NAME)
            writeDatabaseVersionInPreferences()
        }
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        throw RuntimeException("The $Constants.DB_NAME database is not writable.")
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        installOrUpdateIfNecessary()
        return super.getReadableDatabase()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Nothing to do
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Nothing to do
    }

//    companion object {
//        const val Constants.DB_NAME = Constants.DB_NAME
//        const val DATABASE_VERSION = 1
//    }

    fun getAllCompanies(companyType: Int): ArrayList<Company> {

        val allCompanies = ArrayList<Company>()
        val db = readableDatabase
        val selectALLQuery =
            "SELECT *  FROM company c  LEFT JOIN company_types ON company_types.company_id = c.c_id  WHERE company_types.type_id = $companyType and c.status=1"
        Utils.log(selectALLQuery)
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(0)
                    val name = cursor.getString(1)
                    val logo = cursor.getBlob(2)
                    val url = cursor.getString(3)
                    val rating = cursor.getFloat(4)
                    val type = cursor.getInt(5)
                    val email = cursor.getString(8)
                    val address = cursor.getString(11)
                    val link = cursor.getString(12)

                    val company = Company(id)
                    company.name = name
                    company.logo = logo
                    company.url = url
                    company.rating = rating
                    company.email = email
                    company.address = address
                    company.link = link
                    company.type = type

                    allCompanies.add(company)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allCompanies
    }

    fun getPhoneNumbers(companyId: Int): ArrayList<Phone> {

        val allPhones = ArrayList<Phone>()
        val db = readableDatabase
        val selectALLPhonesQuery = "SELECT * FROM phone WHERE company_id == $companyId"
        Utils.log(selectALLPhonesQuery)
        val cursor = db.rawQuery(selectALLPhonesQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(0)
                    val phone = cursor.getString(2)
                    val description = cursor.getString(3)
                    val type = cursor.getInt(4)

                    val phoneNum = Phone(id)
                    phoneNum.phone = phone
                    phoneNum.description = description
                    phoneNum.type = type

                    allPhones.add(phoneNum)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allPhones
    }

    fun getCoverPicture(companyId: Int): ByteArray? {
        val db = readableDatabase
        val selectCoverOfCompany = "SELECT cover_photo FROM company WHERE c_id == $companyId"
        Utils.log(selectCoverOfCompany)
        val cursor = db.rawQuery(selectCoverOfCompany, null)
        var cover: ByteArray? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cover = cursor.getBlob(0)
            }
        }
        cursor.close()
        db.close()
        return cover
    }
}