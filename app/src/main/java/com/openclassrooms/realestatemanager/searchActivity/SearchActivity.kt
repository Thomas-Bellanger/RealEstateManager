package com.openclassrooms.realestatemanager.searchActivity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.Injection
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.ViewModelFactory
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    lateinit var viewModel: ViewModel
    var year: Int? = null
    var month: Int? = null
    private var day: Int? = null
    var type: String = ""
    private val listToRemove = mutableListOf<HomeModel>()
    lateinit var dataViewModel: DataViewModel
    var home: HomeModel? = null
    private val searchViewModel = SearchViewModel.getInstance()
    var surfaceMin: Int = 0
    var surfaceMax: Int = 99999
    var priceMax: Double = 9999999999.0
    var roomNumber: Int = 0
    var bedRoomNumber: Int = 0
    var bathRoomNumber: Int = 0
    var photoNumber: Int = 0
    var date = ""
    var street = ""
    var country = ""
    var postal = ""
    var city = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        viewModel = ViewModel.getInstance()!!
        setContentView(view)
        configureToolbar()
        configureSpinner()
        configureViewModel()
        viewModel.listHomesFiltered.value = mutableListOf()
        binding.searchDate.setOnClickListener { configureDatePicker() }
        binding.searchConfirmBtn.setOnClickListener { startSearching() }

    }

    private fun afterFilter(list:List<HomeModel>){
        finish()
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
        val spinner: Spinner = binding.searchTypeSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.SearchType,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    type = parent.getItemAtPosition(position) as String
                    Log.e("type", type)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    type = parent.getItemAtPosition(0) as String
                }
            }
        }
    }

    private fun configureDatePicker() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        // Date Select Listener.
        val dateSetListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                calendar.set(year, month, dayOfMonth)
                binding.searchDate.text = dateFormat.format(calendar.time)
                date = dateFormat.format(calendar.time)
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

    //button clicked, check and get the info to filter
    private fun startSearching() {
        if (binding.searchSurfaceMin.text.isNotBlank()) {
            surfaceMin = binding.searchSurfaceMin.text.toString().toInt()
        }
        if (binding.searchSurfaceMax.text.isNotBlank()) {
            surfaceMax = binding.searchSurfaceMax.text.toString().toInt()
        }
        if (binding.searchPriceMax.text.isNotBlank()) {
            priceMax = binding.searchPriceMax.text.toString().toDouble()
        }
        if (binding.searchRoom.text.isNotBlank()) {
            roomNumber = binding.searchRoom.text.toString().toInt()
        }
        if (binding.searchBedRoom.text.isNotBlank()) {
            bedRoomNumber = binding.searchBedRoom.text.toString().toInt()
        }
        if (binding.searchBathRoom.text.isNotBlank()) {
            bathRoomNumber = binding.searchBathRoom.text.toString().toInt()
        }
        if (binding.searchPhotoMin.text.isNotBlank()) {
            photoNumber = binding.searchPhotoMin.text.toString().toInt()
        }
        if(binding.searchStreet.text.isNotBlank()){
            street = binding.searchStreet.text.toString()
        }
        if(binding.searchCity.text.isNotBlank()){
            city = binding.searchCity.text.toString()
        }
        if(binding.searchCountry.text.isNotBlank()){
            country = binding.searchCountry.text.toString()
        }
        if(binding.searchPostal.text.isNotBlank()){
            postal = binding.searchPostal.text.toString()
        }
        val dataViewModel: DataViewModel = dataViewModel
        val lifecycleOwner: LifecycleOwner = this

        searchViewModel?.filter(
            binding.searchAvailableBox.isChecked,
            binding.searchSoldBox.isChecked,
            type,
            surfaceMin,
            surfaceMax,
            priceMax,
            roomNumber,
            bedRoomNumber,
            bathRoomNumber,
            photoNumber,
            street,
            city,
            country,
            postal,
            date,
            dataViewModel,
            lifecycleOwner,
            this
        )
        viewModel.listHomesFiltered.observe(this, this::afterFilter)
    }

    private fun configureViewModel() {
        val viewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.dataViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)
    }
}