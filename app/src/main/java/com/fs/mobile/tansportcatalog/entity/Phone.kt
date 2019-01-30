package com.fs.mobile.tansportcatalog.entity


data class Phone(

    var id: Int,

    var companyId: Int,

    var phone: String,

    var type: Int,

    var description: String?
) {
    constructor(id: Int) : this(id, 0, "", 0, "")
}
