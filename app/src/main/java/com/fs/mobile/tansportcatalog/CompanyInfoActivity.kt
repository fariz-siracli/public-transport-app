package com.fs.mobile.tansportcatalog

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
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
        //setSupportActionBar(toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        val selectedCompany = intent.getSerializableExtra("SELECTED_COMPANY") as Company
        tv_company_name.text = selectedCompany.name
        iv_company.setImageBitmap(BitmapFactory.decodeByteArray(selectedCompany.cover, 0, selectedCompany.cover!!.size))
        if (selectedCompany.rating != null) {
            rb_company.visibility = View.VISIBLE
            rb_company.rating = selectedCompany.rating!!
        } else {
            rb_company.visibility = View.GONE
        }
        if (selectedCompany.url != null) {
            tv_company_url.text = selectedCompany.url
            tv_company_url.visibility = View.VISIBLE
        } else {
            tv_company_url.visibility = View.GONE
        }
        if (selectedCompany.email != null) {
            tv_company_email.visibility = View.VISIBLE
            tv_company_email.text = selectedCompany.email
        } else {
            tv_company_email.visibility = View.GONE
        }

        // toolbar.title = null
        toolbar_layout.isTitleEnabled = false;


        AsyncTask.execute {

            val phoneNumbers = getPhoneNumber(selectedCompany)
            if (phoneNumbers != null && phoneNumbers.isNotEmpty()) {

                runOnUiThread {
                    for (phone in phoneNumbers) {
                        val phoneTextView = TextView(this)
                        phoneTextView.text = phone.phone
                        val pad = 5
                        phoneTextView.setPadding(pad, pad, pad, pad)
                        phoneTextView.background = getDrawable(R.drawable.rounded_corner)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            phoneTextView.setTextAppearance(android.R.style.TextAppearance_Medium)
                        }
                        val view = View(this)
                        view.setBackgroundColor(getColor(R.color.bone))
                        val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        lp.bottomMargin = 5
                        lp.topMargin = 5
                        ll_phone_numbers.addView(phoneTextView, lp)
                        //  ll_phone_numbers.addView(view, lp)
                    }

                }
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


                var phoneArray = arrayOfNulls<String>(phoneList!!.size)
                var i = 0

                for (phone in this!!.phoneList!!) {
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

    fun call(number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)

    }

    private fun getPhoneNumber(company: Company): List<Phone> {

        database = AppDatabase.getAppDataBase(this)
        return database!!.phoneDao().getPhonesOfCompany(company.id)
    }
}
