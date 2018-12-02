package com.fs.mobile.tansportcatalog.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.fs.mobile.tansportcatalog.entity.Phone


@Dao
interface PhoneDao {

    @Query("SELECT * FROM phone WHERE company_id == :companyId")
    fun getPhonesOfCompany(companyId: Int):List<Phone>
}