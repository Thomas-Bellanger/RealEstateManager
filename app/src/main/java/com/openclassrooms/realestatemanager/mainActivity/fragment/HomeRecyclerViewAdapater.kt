package com.openclassrooms.realestatemanager.mainActivity.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.HomeModel

class HomeRecyclerViewAdapter(private val homes: List<HomeModel>) :
    RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_list_item, parent, false)
        mContext = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(homes[position])
    }


    override fun getItemCount(): Int {
        return homes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.homeImage)
        lateinit var homeImage: ImageView

        @BindView(R.id.houseType)
        lateinit var type: TextView

        @BindView(R.id.housePrice)
        lateinit var price: TextView

        @BindView(R.id.houseLocation)
        lateinit var city: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun update(home: HomeModel) {
            type.text = home.type
            price.text = home.price.toString()
            city.text = home.city
        }
    }
}