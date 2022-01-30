package com.openclassrooms.realestatemanager.editActivity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.addActivity.PhotoActivity.PhotoActivity
import com.openclassrooms.realestatemanager.adapter.PhotoRecyclerVIewAdapter
import com.openclassrooms.realestatemanager.databinding.ActivityEditBinding
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.Utils
import java.util.*

class EditActivity : AppCompatActivity() {
    private var callBackType: String = ""
    private var callBackMoneyType: String = ""
    private var callBackCity: String = ""
    private var callBackCountry: String = ""
    private var callBackPostal: String = ""
    private var callBackStreet: String = ""
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var callBackSurface: String = ""
    private var callBackRoomNumber: String = ""
    private var callBackPrice: String = ""
    private var callBackBathRoom: String = ""
    private var callBackBedRoom: String = ""
    private var callBackDescription: String = ""
    private var callBackAppartment: String = ""
    var viewModel = com.openclassrooms.realestatemanager.viewModel.ViewModel.getInstance()
    private lateinit var binding: ActivityEditBinding
    var editListPhoto: MutableList<PhotoModel> = ArrayList<PhotoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        val recyclerView = binding.recyclerViewPhotoAdd
        setContentView(view)
        configureToolbar()
        configureSpinner()
        configureMoneySpinner()
        configureAppartmentVisibility(false)
        binding.editButton.setOnClickListener { configureClick() }
        binding.addPhotoBtn.setOnClickListener { photoIntent() }
        val horizontalLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )
        viewModel?.listPhoto?.value?.clear()
        viewModel?.listPhoto?.observe(this, this::initList)
        viewModel?.isAppartment?.observe(this, this::configureAppartmentVisibility)
        configureInfo()
        binding.sellButton.setOnClickListener { confirmSell() }
    }

    private fun configureToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun configureSpinner() {
        var type = binding.addType
        val adapterTypes = ArrayAdapter.createFromResource(
            this,
            R.array.Type,
            android.R.layout.simple_spinner_item
        )
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.adapter = adapterTypes
        type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                callBackType = parent.getItemAtPosition(position) as String
                viewModel?.compareString(callBackType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                callBackType = parent.getItemAtPosition(0) as String
            }
        }
    }

    private fun configureAppartmentVisibility(boolean: Boolean) {
        if (boolean) {
            binding.addAppartment.visibility = View.VISIBLE
        } else {
            binding.addAppartment.text?.clear()
            binding.addAppartment.visibility = View.GONE
        }
    }

    private fun configureMoneySpinner() {
        var moneyType = binding.addMoneyType
        val adapterTypes = ArrayAdapter.createFromResource(
            this,
            R.array.MoneyType,
            android.R.layout.simple_spinner_item
        )
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        moneyType.adapter = adapterTypes
        moneyType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                callBackMoneyType = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                callBackMoneyType = parent.getItemAtPosition(0) as String
            }
        }
    }

    private fun configureInfo() {
        val homeModel = viewModel?.home?.value
        if (binding.addAppartment.visibility == VISIBLE) {
            binding.addAppartment.setText(homeModel?.appartment)
        }
        homeModel?.bathRoomNumber?.let { binding.addBathRoom.setText(it.toString()) }
        homeModel?.roomNumber?.let { binding.addRoom.setText(it.toString()) }
        homeModel?.bedRoomNumber?.let { binding.addBedRoom.setText(it.toString()) }
        homeModel?.surface?.let { binding.addSurface.setText(it.toString()) }
        homeModel?.street?.let { binding.addStreet.setText(it) }
        homeModel?.country?.let { binding.addCountry.setText(it) }
        homeModel?.city?.let { binding.addCity.setText(it) }
        homeModel?.postalCode?.let { binding.addPostal.setText(it) }
        homeModel?.price?.let { binding.addPrice.setText(it.toString()) }
        homeModel?.description?.let { binding.addDescription.setText(it) }
    }

    private fun photoIntent() {
        var photoIntent: Intent? = Intent(this, PhotoActivity::class.java)
        startActivity(photoIntent)
    }

    //update the list
    private fun initList(listPhoto: MutableList<PhotoModel>) {
        var recyclerView = binding.recyclerViewPhotoAdd
        recyclerView.adapter = PhotoRecyclerVIewAdapter(listPhoto)
    }

    private fun configureClick() {
        callBackCity = binding.addCity.text.toString()
        callBackCountry = binding.addCountry.text.toString()
        callBackPostal = binding.addPostal.text.toString()
        callBackStreet = binding.addStreet.text.toString()
        callBackSurface = binding.addSurface.text.toString()
        callBackRoomNumber = binding.addRoom.text.toString()
        callBackPrice = binding.addPrice.text.toString()
        callBackBathRoom = binding.addBathRoom.text.toString()
        callBackDescription = binding.addDescription.text.toString()
        callBackBedRoom = binding.addBedRoom.text.toString()

        when {
            callBackCity.isBlank() -> {
                binding.addCity.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackStreet.isBlank() -> {
                binding.addStreet.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackPostal.isBlank() -> {
                binding.addPostal.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackCountry.isBlank() -> {
                binding.addCountry.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackCountry.isBlank() -> {
                binding.addSurface.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackRoomNumber.isBlank() -> {
                binding.addRoom.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackBathRoom.isBlank() -> {
                binding.addBathRoom.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackPrice.isBlank() -> {
                binding.addPrice.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackDescription.isBlank() -> {
                binding.addDescription.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            callBackBedRoom.isBlank() -> {
                binding.addBedRoom.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                if (callBackMoneyType == "euro") {
                    callBackPrice = Utils.convertEuroToDollar(callBackPrice.toInt()).toString()
                }
                editListPhoto = viewModel?.listPhoto?.value ?: editListPhoto
                viewModel!!.createHomeFireBase(
                    viewModel!!.avatar,
                    callBackType,
                    callBackCity,
                    callBackPrice.toDouble(),
                    callBackStreet,
                    callBackAppartment,
                    callBackPostal,
                    callBackCountry,
                    callBackSurface.toInt(),
                    callBackRoomNumber.toInt(),
                    callBackBathRoom.toInt(),
                    callBackBedRoom.toInt(),
                    "location",
                    "",
                    callBackDescription
                )
                createNotif()
                this.finish()
            }
        }
    }

    private fun confirmSell(){
        binding.editScroll.visibility = INVISIBLE
        binding.editCOnstraint.visibility = VISIBLE
        binding.editConfirmBtn.setOnClickListener { sellHome() }
        binding.editCancelBtn.setOnClickListener { cancel() }
    }

    private fun sellHome(){
        createNotif()
        viewModel?.deletteHome(viewModel?.home?.value!!)
        finish()
    }

    private fun cancel(){
        binding.editScroll.visibility = VISIBLE
        binding.editCOnstraint.visibility = GONE
    }
    private fun createNotif() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val context = baseContext
        viewModel?.sendVisualNotification(context, notificationManager)
    }
}