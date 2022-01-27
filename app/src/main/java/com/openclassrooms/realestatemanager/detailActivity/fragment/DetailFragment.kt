package com.openclassrooms.realestatemanager.detailActivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.viewModel.ViewModel

class DetailFragment : Fragment() {
    private val viewModel: ViewModel? = ViewModel.getInstance()
    var home: HomeModel = HomeModel.testHome
    lateinit var surface: TextView
    lateinit var rooms: TextView
    lateinit var bathRooms: TextView
    lateinit var bedRooms: TextView
    lateinit var adressNumber: TextView
    lateinit var appartment: TextView
    lateinit var city: TextView
    lateinit var postalCode: TextView
    lateinit var country: TextView
    lateinit var description: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val context = view.context
        surface = view.findViewById(R.id.detailSurface)
        rooms = view.findViewById(R.id.detail_Rooms)
        bathRooms = view.findViewById(R.id.detailBathrooms)
        bedRooms = view.findViewById(R.id.detailBedrooms)
        adressNumber = view.findViewById(R.id.detailAdressNumber)
        appartment = view.findViewById(R.id.detailAppartment)
        city = view.findViewById(R.id.detailCity)
        postalCode = view.findViewById(R.id.detailPostal)
        country = view.findViewById(R.id.detailCountry)
        description = view.findViewById(R.id.descriptionText)
        if (viewModel!!.getMyHome().value != null) {
            home = viewModel.getMyHome().value!!
            adjustValue(home)
        }
        viewModel.getMyHome().observe(this.viewLifecycleOwner, this::adjustValue)
        return view
    }

    private fun adjustValue(home: HomeModel) {
        surface.text = home.surface.toString()
        rooms.text = home.roomNumber.toString()
        bathRooms.text = home.bathRoomNumber.toString()
        bedRooms.text = home.bedRoomNumber.toString()
        adressNumber.text = home.street
        appartment.text = if (home.appartment != null) {
            home.appartment
        } else {
            ""
        }
        city.text = home.city
        postalCode.text = home.postalCode
        country.text = home.country
    }

    companion object {
        fun newInstance(): DetailFragment {
            return newInstance()
        }
    }
}