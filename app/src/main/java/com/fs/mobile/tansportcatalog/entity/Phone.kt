package com.fs.mobile.tansportcatalog.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "phone")
data class Phone(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: Int,

    @ColumnInfo(name = "company_id")
    val companyId : Int,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "phone_type")
    val type: Int,

    @ColumnInfo(name = "desc")
    val description: String?
)
