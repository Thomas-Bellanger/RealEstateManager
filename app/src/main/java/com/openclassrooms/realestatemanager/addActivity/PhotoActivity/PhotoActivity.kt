package com.openclassrooms.realestatemanager.addActivity.PhotoActivity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.addActivity.PhotoActivity.adapter.AddPhotoRecyclerViewAdapter
import com.openclassrooms.realestatemanager.databinding.ActivityPhotoBinding
import com.openclassrooms.realestatemanager.editActivity.viewModel.EditViewModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import java.io.FileNotFoundException
import java.util.*


class PhotoActivity : AppCompatActivity(), AddPhotoRecyclerViewAdapter.Callbacks {
    var photoTitle: String? = null
    private val RESULT_LOAD_IMG = 1
    private var listPhoto: MutableList<PhotoModel> = ArrayList<PhotoModel>()
    var image: String? = null
    var viewModel = com.openclassrooms.realestatemanager.viewModel.ViewModel.getInstance()
    private var recyclerView: RecyclerView? = null
    private lateinit var binding: ActivityPhotoBinding
    var avatarView: View? = null
    private var editViewModel = EditViewModel.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.addImage.setOnClickListener { searchPhoto() }
        binding.cancelBtn.setOnClickListener {
            viewModel?.avatar = PhotoModel.NO_PHOTO.image.toString()
            editViewModel?.photoToAdd?.clear()
            editViewModel?.photoToRemove?.clear()
            finish()
        }
        binding.confirmButton.setOnClickListener { confirm() }
        binding.addImageToList.setOnClickListener { addToList() }
        recyclerView = binding.recyclerViewPhoto
        val horizontalLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = horizontalLayoutManager
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL,
            )
        )
        configureOnClickRecyclerView()
        if (viewModel?.listPhoto?.value != null) {
            listPhoto = viewModel?.listPhoto?.value ?: mutableListOf()
        }
        initList(listPhoto)
        viewModel!!.listPhoto.observe(this, this::initList)
        if (viewModel?.avatar != "") {
            setAvatar(viewModel?.avatar)
        }
    }

    //update the list
    private fun initList(photoList: MutableList<PhotoModel>) {
        recyclerView!!.adapter = AddPhotoRecyclerViewAdapter(photoList)
    }

    //search for photos
    private fun searchPhoto() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                val imageUri: Uri = data?.data!!
                image = imageUri.toString()
                setView()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "An error happens...", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(applicationContext, "You don't have photo", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun confirm() {
        if (viewModel?.avatar == PhotoModel.NO_PHOTO.image.toString()) {
            binding.confirmButton.error = "You need a main photo!"
        } else {
            finish()
        }
    }

    //put the photo into the imageview
    private fun setView() {
        val photoView = binding.imageImageView
        Glide.with(photoView.context)
            .load(image)
            .into(photoView)
    }

    //check photomodel info and add it to list
    private fun addToList() {
        photoTitle = binding.photoTitle.text.toString()
        if (photoTitle.isNullOrEmpty()) {
            binding.photoTitle.error = "The photo need a title"
        } else if (image.isNullOrEmpty()) {
            binding.addImage.error = "The photo need a picture"
        } else {
            val photoToAdd = PhotoModel(homeUid = 0, title = photoTitle!!, image = image!!)
            listPhoto.add(photoToAdd)
            editViewModel?.photoToAdd?.add(photoToAdd)
            viewModel?.listPhoto?.value = listPhoto
            binding.photoTitle.text.clear()
            binding.imageImageView.setImageDrawable(null)
        }
    }

    //to choose property main image(avatar)
    private fun configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView!!, R.layout.media_list_item)
            .setOnItemClickListener { _, position, view ->
                if (avatarView != null) {
                    avatarView?.setBackgroundColor(Color.WHITE)
                }
                view.setBackgroundColor(Color.GREEN)
                avatarView = view
                setAvatar(listPhoto[position].image)
            }
    }

    //show the chosen avatar
    private fun setAvatar(image: String?) {
        val photoView = binding.avatarImageView
        Glide.with(photoView.context)
            .load(this.image)
            .into(photoView)
        if (image != null) {
            viewModel?.avatar = image
        }
    }

    //to suppress photo from the list
    override fun onClickResponse(int: Int) {
        editViewModel?.photoToRemove?.add(listPhoto[int])
        editViewModel?.photoToAdd?.remove(listPhoto[int])
        listPhoto.removeAt(int)
        viewModel!!.listPhoto.value = listPhoto
    }
}