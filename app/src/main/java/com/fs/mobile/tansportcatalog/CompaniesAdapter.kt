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
import com.fs.mobile.tansportcatalog.entity.Company
import com.fs.mobile.tansportcatalog.entity.Phone
import kotlinx.android.synthetic.main.rv_item_row.view.*


class CompaniesAdapter(var items: List<Company>, val activity: MainActivity) : RecyclerView.Adapter<ViewHolder>() {

    private val MY_PERMISSIONS_REQUEST_CALL_PHONE: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_item_row, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var current = items.get(position)
        holder?.titleTextview?.text = current.name
        if (current.logo != null)
            holder?.companyLogo.setImageBitmap(BitmapFactory.decodeByteArray(current.logo, 0, current.logo!!.size))
        holder.container.setOnClickListener {

            AsyncTask.execute {
                database = AppDatabase.getAppDataBase(activity)
                val phoneList = database!!.phoneDao().getPhonesOfCompany(current.id)
                if (phoneList != null) {
                    activity.runOnUiThread {
                        Toast.makeText(activity, current.name + " - " + phoneList[0].phone, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        holder.callBtn.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {

                } else {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        MY_PERMISSIONS_REQUEST_CALL_PHONE
                    )
                }
            }
            AsyncTask.execute {
                var phoneNumber = getPhoneNumber(current)
                val shortNum = phoneNumber[0].phone
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$shortNum")
                activity.runOnUiThread { activity.startActivity(callIntent) }

            }
        }
    }

    fun getPhoneNumber(company: Company): List<Phone> {

        database = AppDatabase.getAppDataBase(activity)
        return database!!.phoneDao().getPhonesOfCompany(company.id)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }


}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var titleTextview = view.tv_item_title;
    var companyLogo = view.iv_profile_image
    var container = view.card_view
    var callBtn = view.btn_call
}