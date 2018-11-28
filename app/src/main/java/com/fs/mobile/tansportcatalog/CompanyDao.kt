package com.fs.mobile.tansportcatalog

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.entity.CompanyTypeRelation

@Dao
interface CompanyDao {

//    @Query("SELECT * FROM Company_types WHERE  type_id ==  :companyType")
   /* @Query("SELECT  c.* from Company_types ct  " +
            "left join Company c on c._id = ct.company_id" +
            "where ct.type_id == :companyType")*/
    @Query("SELECT company_id  FROM company_types" )
            //", Company  INNER JOIN CompanyTypeRelation ON company_id = Company._id  WHERE type_id == :companyType")
    fun getCompanyByType(companyType: Int): List<Int>


}