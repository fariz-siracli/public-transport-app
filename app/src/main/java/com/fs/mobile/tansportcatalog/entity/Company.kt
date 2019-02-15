package com.fs.mobile.tansportcatalog.entity

import android.util.Log
import java.io.Serializable


data class Company(
    var id: Int,
    var name: String,
    var logo: ByteArray?,
    var url: String?,
    var rating: Float?,
    var type: Int,
    val sortOrder: Int,
    var city: Int?,
    var email: String?,
    var cover: ByteArray?,
    var status: Boolean,
    var address: String?,
    var link: String?,
    var about: String?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Company

        if (id != other.id) return false

        return true
    }

    constructor(id: Int) : this(id, "", null, "", 0.0f, 0, 0, 0, "", null, false, "", "", "") {
        Log.d("secondary", "Hello");
    }

    override fun hashCode(): Int {
        return id
    }
}