package com.fs.mobile.tansportcatalog

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.fs.mobile.tansportcatalog.entity.Company

@Dao
interface CompanyDao {

    @Query("SELECT * FROM Company WHERE  type ==  :companyType")
    fun getCompanyByType(companyType: Int): List<Company>
}