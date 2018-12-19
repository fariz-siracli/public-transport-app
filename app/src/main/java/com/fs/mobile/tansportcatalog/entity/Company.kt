package com.fs.mobile.tansportcatalog.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "company")

data class Company(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var logo: ByteArray?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "rating")
    val rating: Float?,

    @ColumnInfo(name = "type")
    val type: Int,

    @ColumnInfo(name = "sort_order")
    val sortOrder: Int,

    @ColumnInfo(name = "city")
    val city: Int?,

    @ColumnInfo(name = "email")
    val email: String?,


    @ColumnInfo(name = "cover_photo")
    val cover: ByteArray?,

    @ColumnInfo(name = "status")
    val status: Boolean
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Company

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}