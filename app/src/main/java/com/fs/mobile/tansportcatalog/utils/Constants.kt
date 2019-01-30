package com.fs.mobile.tansportcatalog.utils

class Constants{
    companion object {
        const val DB_NAME = "pub_tr.db"
        var DB_NAME_FULL_NAME = "pub_tr.db"
        val CREATE_SCRIPT = "CREATE TABLE  company_1 ( 'c_id' INTEGER, 'name' TEXT, 'logo' BLOB, 'url' TEXT, 'rating' NUMERIC, 'type' INTEGER, 'sort_order' INTEGER, 'city' INTEGER, 'email' TEXT, 'cover_photo' BLOB, 'status' INTEGER DEFAULT 1, 'address' TEXT, 'app_link' TEXT )"
        val INSERT_SCRIPT = "INSERT INTO company_1 (c_id, name, logo, url, rating, type, sort_order, city, email, cover_photo, status, address, app_link) " +
                " SELECT c_id, name , logo , url , rating, type, sort_order, city, email, cover_photo, status, address, app_link from company"
        @JvmField
        var language: String = "az"
        @JvmField
        var SAVED_USER_LANGUAGE: String = "az"
        val DROP_TABLE = "DROP TABLE company"
        val RENAME_SCRIPT ="ALTER TABLE company_1 RENAME TO company"
        val DATABASE_VERSION =1
    }
}