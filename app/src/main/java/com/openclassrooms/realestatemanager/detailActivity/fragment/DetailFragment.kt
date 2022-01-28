package com.openclassrooms.realestatemanager.detailActivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PhotoRecyclerVIewAdapter
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.utils.Utils
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
    lateinit var price:TextView
    lateinit var symbol:ImageView
    lateinit var recyclerView:RecyclerView

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
        price = view.findViewById(R.id.detailPrice)
        symbol = view.findViewById(R.id.detailMoneySymbol)
        recyclerView = view.findViewById(R.id.recyclerViewRooms)
        if (viewModel!!.getMyHome().value != null) {
            home = viewModel.getMyHome().value!!
            adjustValue(home)
        }
        viewModel.getMyHome().observe(this.viewLifecycleOwner, this::adjustValue)
        viewModel.moneyType.observe(this.viewLifecycleOwner, this::changeMoney)
        recyclerView.adapter = PhotoRecyclerVIewAdapter(home.listPhoto)
        return view
    }

    private fun adjustValue(home: HomeModel) {
        description.text = home.description
        surface.text = home.surface.toString()
        rooms.text = home.roomNumber.toString()
        bathRooms.text = home.bathRoomNumber.toString()
        bedRooms.text = home.bedRoomNumber.toString()
        adressNumber.text = home.street
        home.appartment.let {
            appartment.text = it ?:""
        }
        if(appartment.text.isNullOrEmpty()){
            appartment.visibility= GONE
        }
        city.text = home.city
        postalCode.text = home.postalCode
        country.text = home.country
        description.text = home.description
    }

    private fun changeMoney(money: ViewModel.MoneyType){
        if (viewModel!!.moneyType.value!! == ViewModel.MoneyType.DOLLAR) {
            symbol.setImageDrawable(getDrawable(requireContext(),R.drawable.baseline_attach_money_green_a700_24dp))
            price.text = home.price.toString()
        } else if (viewModel.moneyType.value!! == ViewModel.MoneyType.EURO) {
            price.text = Utils.convertDollarToEuro(home.price!!.toInt()).toString()
            symbol.setImageDrawable(getDrawable(requireContext(),R.drawable.baseline_euro_symbol_yellow_700_24dp))
        }
    }
}