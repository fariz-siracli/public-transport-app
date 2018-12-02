package com.fs.mobile.tansportcatalog.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey


@Entity(tableName = "company_types", primaryKeys = ["company_id", "type_id"],
    foreignKeys = [
        ForeignKey(entity = Company::class,
            parentColumns = ["_id"],
            childColumns = ["company_id"]),
        ForeignKey(entity = Type::class,
            parentColumns = ["_id"],
            childColumns = ["type_id"])
    ])
data class CompanyTypeRelation(
    val company_id: Long,
    val type_id: Long
)