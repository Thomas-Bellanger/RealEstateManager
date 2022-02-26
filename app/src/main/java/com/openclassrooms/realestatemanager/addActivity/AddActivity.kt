package com.openclassrooms.realestatemanager.addActivity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.openclassrooms.realestatemanager.addActivity.viewmodel.AddViewModel
import com.openclassrooms.realestatemanager.databinding.ActivityAddBinding
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.Injection
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.ViewModelFactory


class AddActivity : AppCompatActivity() {

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
    private lateinit var binding: ActivityAddBinding
    var addViewModel = AddViewModel.getInstance()
    var viewModel = ViewModel.getInstance()
    lateinit var dataViewModel: DataViewModel
    private var callBackAgent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        configureToolbar()
        configureSpinner()
        configureMoneySpinner()
        configureViewModel()
        configureApartmentVisibility(false)
        binding.createButton.setOnClickListener { configureClickCreation() }
        binding.addPhotoBtn.setOnClickListener { photoIntent() }
        val horizontalLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPhotoAdd.layoutManager = horizontalLayoutManager
        binding.recyclerViewPhotoAdd.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )
        viewModel?.listPhoto?.value?.clear()
        viewModel?.listPhoto?.observe(this, this::initList)
        addViewModel?.isAppartment?.observe(this, this::configureApartmentVisibility)
        addViewModel?.avatar = PhotoModel.NO_PHOTO.image.toString()
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
        val type = binding.addType
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
                addViewModel?.compareString(callBackType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                callBackType = parent.getItemAtPosition(0) as String
            }
        }
    }

    //show appartment number if the property is an appartment
    private fun configureApartmentVisibility(boolean: Boolean) {
        if (boolean) {
            binding.addAppartment.visibility = VISIBLE
        } else {
            binding.addAppartment.text?.clear()
            binding.addAppartment.visibility = GONE
        }
    }

    //set spinner for money type
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

    //intent to search photos
    private fun photoIntent() {
        val photoIntent: Intent = Intent(this, PhotoActivity::class.java)
        startActivity(photoIntent)
    }

    //update the list
    private fun initList(listPhoto: MutableList<PhotoModel>) {
        binding.recyclerViewPhotoAdd.adapter = PhotoRecyclerVIewAdapter(listPhoto)
    }

    //settings to confirm the creation
    private fun confirmCreation() {
        binding.addScroll.visibility = View.INVISIBLE
        binding.addConstraint.visibility = VISIBLE
        binding.addConfirmBtn.setOnClickListener { creation() }
        binding.addCancelBtn.setOnClickListener { cancel() }
    }

    //cancel
    private fun cancel() {
        binding.addScroll.visibility = VISIBLE
        binding.addConstraint.visibility = GONE
    }

    //check if there is enough information to create home and create it
    private fun configureClickCreation() {

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
            viewModel?.listPhoto?.value.isNullOrEmpty() -> {
                binding.addPhotoBtn.error = "need photos"
            }
            else -> {
                if (callBackMoneyType == "euro") {
                    callBackPrice = Utils.convertEuroToDollar(callBackPrice.toInt()).toString()
                }
                confirmCreation()
            }
        }
    }

    private fun creation(){
        callBackAgent = binding.agent.text.toString()
        if( callBackAgent.isBlank()){
            binding.agent.error = "Enter the name of the agent in charge"
        }
        else{
            addViewModel!!.createHomeFireBase(
                dataViewModel,
                Utils.isConnected(this),
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
                callBackDescription,
                callBackAgent,
                binding.checkTrain.isChecked,
                binding.checkShop.isChecked,
                binding.checkSchool.isChecked,
                binding.checkPark.isChecked,
                this
            )
            createNotif()
            viewModel?.avatar = PhotoModel.NO_PHOTO.image.toString()
            this.finish()
        }
    }

    //create notification
    private fun createNotif() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val context = baseContext
        viewModel?.sendVisualNotification(context, notificationManager)
    }

    private fun configureViewModel() {
        val viewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.dataViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)

    }
}