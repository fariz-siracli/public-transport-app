package com.fs.mobile.tansportcatalog

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.fs.mobile.tansportcatalog.db.AppDatabase
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.entity.Phone
import com.fs.mobile.tansportcatalog.utils.Utils
import kotlinx.android.synthetic.main.activity_company_info.*
import kotlinx.android.synthetic.main.content_company_info.*


class CompanyInfoActivity : AppCompatActivity() {
    var phoneList: List<Phone>? = null

    private val MY_PERMISSIONS_REQUEST_CALL_PHONE = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_info)

//        val w = window // in Activity's onCreate() for instance
//        w.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        // window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        // window.setStatusBarColor(getColor(R.color.status_bar));

        val selectedCompany = intent.getSerializableExtra("SELECTED_COMPANY") as Company
        tv_company_name.text = selectedCompany.name
        iv_company.setImageBitmap(BitmapFactory.decodeByteArray(selectedCompany.cover, 0, selectedCompany.cover!!.size))
        if (selectedCompany.rating != null) {
            // rb_company.visibility = View.VISIBLE
            rb_company.rating = selectedCompany.rating!!
        } else {
            //  rb_company.visibility = View.GONE
        }
        if (selectedCompany.address != null) {
            tv_company_address.text = selectedCompany.address
        }
        if (selectedCompany.url != null && !selectedCompany.url.equals("")) {
            tv_company_url.text = selectedCompany.url
            tv_company_url.visibility = View.VISIBLE
        } else {
            ll_url.visibility = View.GONE
        }

        if (selectedCompany.email != null) {
            ll_email.visibility = View.VISIBLE
            tv_company_email.text = selectedCompany.email
        } else {
            ll_email.visibility = View.GONE
        }

        toolbar_layout.isTitleEnabled = false


        AsyncTask.execute {

            val phoneNumbers = getPhoneNumber(selectedCompany)
            if (phoneNumbers != null && phoneNumbers.isNotEmpty()) {

                runOnUiThread {
                    for (phone in phoneNumbers) {
                        val phoneListRow: LinearLayout =
                            layoutInflater.inflate(R.layout.phone_number_layout, null) as LinearLayout
                        if (phone.type == 4) {
                            phoneListRow.findViewById<TextView>(R.id.tv_phone_icon)
                                .setBackgroundResource(R.drawable.ic_icons8_whatsapp)
                        } else {
                            phoneListRow.findViewById<TextView>(R.id.tv_phone_icon)
                                .setBackgroundResource(R.drawable.ic_baseline_phone_24px)
                        }
                        if (phone.phone.length > 7) {
                            var phoneText = phone.phone.replace("994", "0").replace("+", "")
                            phoneText = "(" + phoneText.substring(0, 3) + ") " + phoneText.substring(3)
                            phoneListRow.findViewById<TextView>(R.id.tv_phone).text = phoneText
                        } else {
                            phoneListRow.findViewById<TextView>(R.id.tv_phone).text = phone.phone
                        }
                        val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        lp.bottomMargin = 5
                        lp.topMargin = 5
                        ll_phone_numbers.addView(phoneListRow, lp)
                    }

                }
            }
            if (selectedCompany.link != null && !selectedCompany.link.equals("")) {
                ll_app_url.visibility = View.VISIBLE
                tv_app_url.setOnClickListener { openAppView(selectedCompany) }
                val content = SpannableString(getString(R.string.get_the_app))
                content.setSpan(UnderlineSpan(), 0, content.length, 0)
                tv_app_url.text = content
            } else {
                ll_app_url.visibility = View.GONE
            }


        }


        btn_back.setOnClickListener {
            finish()
        }
        btn_call_other.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_CALL_PHONE
                )
                Utils.log("permitted")
            } else {


                val phoneArray = arrayOfNulls<String>(phoneList!!.size)
                var i = 0

                for (phone in this.phoneList!!) {
                    phoneArray[i++] = phone.phone
                }
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.select_number_to_call)
                builder.setItems(phoneArray) { dialog, which -> call(phoneArray!![which]!!) }
                builder.show()
            }
        }
        AsyncTask.execute {
            database = AppDatabase.getAppDataBase(this)
            phoneList = database!!.phoneDao().getPhonesOfCompany(selectedCompany.id)
        }
    }

    fun call(num: String) {
        val number: String
        if (num.length > 5)
            number = "+" + num
        else
            number = num
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)

    }

    private fun getPhoneNumber(company: Company): List<Phone> {

        database = AppDatabase.getAppDataBase(this)
        return database!!.phoneDao().getPhonesOfCompany(company.id)
    }

    fun openAppView(company: Company) {
        val ty = packageManager.getLaunchIntentForPackage(company.link!!)
        if (ty != null)
            startActivity(ty)
        else {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${company.link}")))
        }
    }
}
