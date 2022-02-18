package com.openclassrooms.realestatemanager.editActivity

import android.app.DatePickerDialog
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PhotoRecyclerVIewAdapter
import com.openclassrooms.realestatemanager.addActivity.PhotoActivity.PhotoActivity
import com.openclassrooms.realestatemanager.databinding.ActivityEditBinding
import com.openclassrooms.realestatemanager.editActivity.viewModel.EditViewModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.Injection
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.ViewModelFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
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
    private var callBackApartment: String = ""
    private var sellDate = ""
    private var callBackAgent = ""
    var editViewModel = EditViewModel.getInstance()
    var viewModel = ViewModel.getInstance()
    private lateinit var binding: ActivityEditBinding
    var editListPhoto: MutableList<PhotoModel> = ArrayList<PhotoModel>()
    lateinit var dataViewModel: DataViewModel
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        val recyclerView = binding.recyclerViewPhotoAdd
        setContentView(view)
        configureToolbar()
        configureSpinner()
        configureMoneySpinner()
        configureViewModel()
        viewModel?.avatar = viewModel?.home?.value?.avatar.toString()
        binding.sellDateChosen.setOnClickListener { configureDatePicker() }
        configureApartmentVisibility(false)
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
        editViewModel?.photoToRemove?.clear()
        editViewModel?.photoToAdd?.clear()
        dataViewModel.getPhotos(viewModel?.home?.value?.uid).observe(this, this::setListPhoto)
        viewModel?.listPhoto?.observe(this, this::initList)
        editViewModel?.isAppartment?.observe(this, this::configureApartmentVisibility)
        configureInfo()
        viewModel?.avatar = viewModel?.home?.value?.avatar.toString()
        binding.sellButton.setOnClickListener { confirmSell() }
    }

    private fun setListPhoto(listPhoto: List<PhotoModel>) {
        viewModel?.listPhoto?.value = listPhoto as MutableList
    }

    //toolbar
    private fun configureToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    //set the spinner to choose property type
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
                editViewModel?.compareString(callBackType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                callBackType = parent.getItemAtPosition(0) as String
            }
        }
    }

    //show apartment number if the property is an appartment
    private fun configureApartmentVisibility(boolean: Boolean) {
        if (boolean) {
            binding.addAppartment.visibility = View.VISIBLE
        } else {
            binding.addAppartment.text?.clear()
            binding.addAppartment.visibility = View.GONE
        }
    }

    //set spinner for money type
    private fun configureMoneySpinner() {
        val moneyType = binding.addMoneyType
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

    //set the views with home's information
    private fun configureInfo() {
        val homeModel = viewModel?.home?.value
        if (homeModel != null) {
            if (binding.addAppartment.visibility == VISIBLE) {
                binding.addAppartment.setText(homeModel.appartment)
            }
            homeModel.bathRoomNumber.let { binding.addBathRoom.setText(it.toString()) }
            homeModel.roomNumber.let { binding.addRoom.setText(it.toString()) }
            homeModel.bedRoomNumber.let { binding.addBedRoom.setText(it.toString()) }
            homeModel.surface.let { binding.addSurface.setText(it.toString()) }
            homeModel.street.let { binding.addStreet.setText(it) }
            homeModel.country.let { binding.addCountry.setText(it) }
            homeModel.city.let { binding.addCity.setText(it) }
            homeModel.postalCode.let { binding.addPostal.setText(it) }
            homeModel.price.let { binding.addPrice.setText(it.toString()) }
            homeModel.description.let { binding.addDescription.setText(it) }
            binding.checkPark.isChecked = homeModel.park
            binding.checkSchool.isChecked = homeModel.school
            binding.checkShop.isChecked = homeModel.shops
            binding.checkTrain.isChecked = homeModel.station
            binding.editInCharge.setText(homeModel.sellerName)
        }
    }

    //intent to search photos
    private fun photoIntent() {
        val photoIntent: Intent? = Intent(this, PhotoActivity::class.java)
        startActivity(photoIntent)
    }

    //update the list
    private fun initList(listPhoto: MutableList<PhotoModel>) {
        val recyclerView = binding.recyclerViewPhotoAdd
        recyclerView.adapter = PhotoRecyclerVIewAdapter(listPhoto)
    }

    //check info and edit the property
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
        callBackAgent = binding.editInCharge.text.toString()
        if (binding.addAppartment.visibility != GONE) {
            callBackApartment = binding.addAppartment.text.toString()
        }
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
            callBackAgent.isBlank() ->{
                binding.editInCharge.error = "need a value!"
                Toast.makeText(this, "Some information are not complete!", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                if (callBackMoneyType == "euro") {
                    callBackPrice = Utils.convertEuroToDollar(callBackPrice.toInt()).toString()
                }
                editListPhoto = viewModel?.listPhoto?.value ?: editListPhoto

                editViewModel?.editHome(
                    dataViewModel,
                    this,
                    callBackCity,
                    callBackCountry,
                    callBackPostal,
                    callBackStreet,
                    callBackSurface.toInt(),
                    callBackRoomNumber.toInt(),
                    callBackPrice.toDouble(),
                    callBackBathRoom.toInt(),
                    callBackDescription,
                    callBackBedRoom.toInt(),
                    callBackApartment,
                    callBackAgent,
                    binding.checkTrain.isChecked,
                    binding.checkShop.isChecked,
                    binding.checkSchool.isChecked,
                    binding.checkPark.isChecked
                )
                createNotif()
                this.finish()
            }
        }
    }

    //settings to confirm the sell
    private fun confirmSell() {
        binding.editScroll.visibility = INVISIBLE
        binding.editConstraint.visibility = VISIBLE
        binding.editConfirmBtn.setOnClickListener { sellHome() }
        binding.editCancelBtn.setOnClickListener { cancel() }
    }

    //confirm home's selling
    private fun sellHome() {
        if (binding.sellDateChosen.text == "Choose the date the property has been sold then confirm!") {
            binding.sellDateChosen.error = "Click on this to choose a date"
        } else {
            viewModel?.home?.value?.isSold = true
            viewModel?.home?.value?.sellTime =
                sellDate
            createNotif()
            viewModel?.home?.value?.let {
                editViewModel?.editHome(
                    dataViewModel,
                    this,
                    callBackCity,
                    callBackCountry,
                    callBackPostal,
                    callBackStreet,
                    callBackSurface.toInt(),
                    callBackRoomNumber.toInt(),
                    callBackPrice.toDouble(),
                    callBackBathRoom.toInt(),
                    callBackDescription,
                    callBackBedRoom.toInt(),
                    callBackApartment,
                    callBackAgent,
                    binding.checkTrain.isChecked,
                    binding.checkShop.isChecked,
                    binding.checkSchool.isChecked,
                    binding.checkPark.isChecked
                )
            }
            finish()
        }
    }

    private fun configureDatePicker() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        // Date Select Listener.
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                calendar.set(year, month, dayOfMonth)
                sellDate = dateFormat.format(calendar.time)
                binding.sellDateChosen.text = "Sold the  $sellDate. Confirm?"
            }
        // Create DatePickerDialog (Spinner Mode):
        val datePickerDialog = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            dateSetListener, year!!, month!!, day!!
        )
        // Show
        datePickerDialog.show()
    }

    //cancel selling
    private fun cancel() {
        binding.editScroll.visibility = VISIBLE
        binding.editConstraint.visibility = GONE
    }

    //create notification
    private fun createNotif() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val context = baseContext
        viewModel?.sendVisualNotification(context, notificationManager)
    }

    //dataViewModel
    private fun configureViewModel() {
        val viewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.dataViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)
    }
}