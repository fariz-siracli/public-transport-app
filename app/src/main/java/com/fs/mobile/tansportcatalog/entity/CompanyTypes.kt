package com.fs.mobile.tansportcatalog.entity

import android.arch.persistence.room.*


@Entity(tableName = "company_types")
data class CompanyTypes(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: Int,

    @ColumnInfo(name = "company_id")
    val companyId: Int,

    @ColumnInfo(name = "type_id")
    val typeId: Int

)



