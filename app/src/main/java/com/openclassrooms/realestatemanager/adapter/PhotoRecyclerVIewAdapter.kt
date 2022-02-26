package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.PhotoModel


class PhotoRecyclerVIewAdapter(private val photoList: List<PhotoModel>) :

    RecyclerView.Adapter<PhotoRecyclerVIewAdapter.ViewHolder>() {

    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_list_item, parent, false)
        mContext = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(photoList[position])
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @BindView(R.id.mediaListItemImage)
        lateinit var photoImage: ImageView

        @BindView(R.id.mediaListItemText)
        lateinit var name: TextView

        @BindView(R.id.mediaListItemDelette)
        lateinit var delette: ImageButton

        init {
            ButterKnife.bind(this, itemView)
        }

        fun update(photo: PhotoModel) {
            name.text = photo.title
            Glide.with(photoImage.context)
                .load(photo.image)
                .into(photoImage)
        }
    }
}