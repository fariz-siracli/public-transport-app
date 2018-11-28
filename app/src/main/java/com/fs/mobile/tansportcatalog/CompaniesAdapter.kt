package com.fs.mobile.tansportcatalog

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fs.mobile.tansportcatalog.entity.Company
import kotlinx.android.synthetic.main.rv_item_row.view.*


class CompaniesAdapter(var items: List<Company>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var current = items.get(position)
        holder?.titleTextview?.text = current.name
        if(current.logo != null)
            holder?.companyLogo.setImageBitmap(BitmapFactory.decodeByteArray(current.logo, 0, current.logo!!.size))
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
}