package com.fs.mobile.tansportcatalog.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.fs.mobile.tansportcatalog.entity.Company


@Dao
interface CompanyDao {

    @Query("SELECT *  FROM company c  LEFT JOIN company_types ON company_types.company_id = c._id  WHERE company_types.type_id == :companyType")
    fun getCompanyByType(companyType: Int): List<Company>


}