package com.openclassrooms.realestatemanager.detailActivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PhotoRecyclerVIewAdapter
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.LocationModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.Injection
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.ViewModelFactory
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class DetailFragment : Fragment() {
    private val viewModel: ViewModel? = ViewModel.getInstance()
    var home: HomeModel? = null
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
    lateinit var price: TextView
    lateinit var symbol: ImageView
    lateinit var createTime: TextView
    lateinit var sellTime: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var detailImage: ImageView
    lateinit var carouselView: CarouselView
    var dataViewModel: DataViewModel? = null
    var listPhoto: MutableList<PhotoModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val context = view.context
        if (viewModel?.home?.value == null) {
            view.visibility = INVISIBLE
        }
        configureViewModel()
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
        listPhoto.add(PhotoModel.NO_PHOTO)
        createTime = view.findViewById(R.id.creationTime)
        sellTime = view.findViewById(R.id.sellTime)
        detailImage = view.findViewById(R.id.detailImage)
        carouselView = view.findViewById(R.id.carouselView)
        viewModel?.home?.observe(this.viewLifecycleOwner, this::adjustValue)
        viewModel?.moneyType?.observe(this.viewLifecycleOwner, this::changeMoney)

        return view
    }

    //adjust value with home
    private fun adjustValue(home: HomeModel) {
        view?.visibility = VISIBLE
        dataViewModel?.getPhotos(home.uid)?.observe(this.viewLifecycleOwner, this::initListPhoto)
        description.text = home.description
        surface.text = home.surface.toString()
        rooms.text = home.roomNumber.toString()
        bathRooms.text = home.bathRoomNumber.toString()
        bedRooms.text = home.bedRoomNumber.toString()
        adressNumber.text = home.street
        home.appartment.let {
            appartment.text = it ?: ""
        }
        if (appartment.text.isNullOrEmpty()) {
            appartment.visibility = GONE
        }
        city.text = home.city
        postalCode.text = home.postalCode
        country.text = home.country
        description.text = home.description
        createTime.text = "On-sale date:" + home.creationTime
        if (home.isSold) {
            sellTime.visibility = VISIBLE
            sellTime.text = "Date of sale:" + home.sellTime
        }
        dataViewModel?.getLocation(home.uid)?.observe(this, this::getStaticMap)
    }

    private fun getStaticMap(location: LocationModel) {
        Glide.with(detailImage.context)
            .load(location.url)
            .into(detailImage)
    }

    //change price with money type
    private fun changeMoney(money: ViewModel.MoneyType) {
        home = viewModel?.home?.value
        if (viewModel!!.moneyType.value!! == ViewModel.MoneyType.DOLLAR) {
            symbol.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.baseline_attach_money_green_a700_24dp
                )
            )
            price.text = home?.price.toString()
        } else if (viewModel.moneyType.value!! == ViewModel.MoneyType.EURO) {
            price.text = home?.price?.toInt()?.let { Utils.convertDollarToEuro(it).toString() }
            symbol.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.baseline_euro_symbol_yellow_700_24dp
                )
            )
        }
    }

    //init ans refresh photo's list
    private fun initListPhoto(list: List<PhotoModel>) {
        val imageListener =
            ImageListener { position, imageView ->
                Glide.with(imageView.context)
                    .load(list[position].image)
                    .into(imageView)
            }
        carouselView.setImageListener(imageListener)
        recyclerView.adapter = PhotoRecyclerVIewAdapter(list)
        carouselView.pageCount = list.size
    }

    //dataViewModel
    private fun configureViewModel() {
        val viewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(context)
        this.dataViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)
    }
}