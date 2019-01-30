package com.fs.mobile.tansportcatalog


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fs.mobile.tansportcatalog.db.DbHelper
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.entity.Phone
import com.fs.mobile.tansportcatalog.utils.Utils
import kotlinx.android.synthetic.main.rv_item_row.view.*


class CompaniesAdapter(var items: List<Company>, private val activity: MainActivity) :
    RecyclerView.Adapter<ViewHolder>() {

    private val MY_PERMISSIONS_REQUEST_CALL_PHONE: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_item_row, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items.isNotEmpty() && position < items.size) {
            val current = items[position]
            holder.titleTextview?.text = current.name
            if (current.logo != null)
                holder.companyLogo.setImageBitmap(BitmapFactory.decodeByteArray(current.logo, 0, current.logo!!.size))
            else {
                holder.companyLogo.setImageDrawable(Utils.getResource(activity, R.drawable.evacuation))
            }
            if (current.type == 1) {
                holder.callBtn.setImageDrawable(Utils.getResource(activity, R.drawable.ic_baseline_android_24px))
            } else {
                holder.callBtn.setImageDrawable(Utils.getResource(activity, R.drawable.ic_baseline_call_green))
            }
            holder.container.setOnClickListener {

                AsyncTask.execute {
                    val intent = Intent(activity, CompanyInfoActivity::class.java)
                    current.logo = null
                    intent.putExtra("SELECTED_COMPANY", current)
                    activity.startActivity(intent)
                }
            }

            holder.callBtn.setOnClickListener {
                if (current.type == 1) {
                    openAppView(current)
                } else {

                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.CALL_PHONE),
                            MY_PERMISSIONS_REQUEST_CALL_PHONE
                        )
                        Utils.log("permitted")
                    } else {

                        AsyncTask.execute {

                            val phoneNumber = getPhoneNumber(current)
                            if (phoneNumber != null && phoneNumber.isNotEmpty()) {
                                val shortNum = phoneNumber[0].phone
                                val callIntent = Intent(Intent.ACTION_CALL)
                                callIntent.data = Uri.parse("tel:$shortNum")
                                activity.runOnUiThread { activity.startActivity(callIntent) }
                            } else {
                                showMessage()

                            }
                        }
                    }
                }
            }
        }
        if (position == items.lastIndex) {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 30
            holder.itemView.layoutParams = params
        } else {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 0
            holder.itemView.layoutParams = params
        }
    }

    private fun showMessage() {
        activity.runOnUiThread {
            Toast.makeText(activity.applicationContext, activity.getString(R.string.phone_not_found), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun openAppView(company: Company) {
        val ty = activity.packageManager.getLaunchIntentForPackage(company.link!!)
        if (ty != null)
            activity.startActivity(ty)
        else {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${company.link}")))
        }
    }

    private fun getPhoneNumber(company: Company): List<Phone> {
        val dbHelper = DbHelper(activity)
        var phoneList = dbHelper.getPhoneNumbers(company.id)
        dbHelper.close()
        return phoneList
    }

    override fun getItemCount(): Int {
        return items.size
    }


}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var titleTextview = view.tv_item_title;
    var companyLogo = view.iv_profile_image
    var container = view.card_view
    var callBtn = view.btn_call
}