package com.openclassrooms.realestatemanager.mainActivity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.HomeDao
import com.openclassrooms.realestatemanager.domain.repository.HomeDataRepository
import com.openclassrooms.realestatemanager.mainActivity.Injection
import com.openclassrooms.realestatemanager.mainActivity.MainViewModel
import com.openclassrooms.realestatemanager.mainActivity.ViewModelFactory
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import java.util.*
import javax.security.auth.callback.Callback


class RecyclerViewFragment : Fragment(), Callback {

    private var recyclerView: RecyclerView? = null
    var homes: MutableList<HomeModel> = ArrayList()
    private val viewModel: ViewModel? = ViewModel.getInstance()
    var mainViewModel:MainViewModel?=null
    private val mMainViewModel: MainViewModel? = null
    private val HOME_ID:Long=1

    interface Callbacks {
        fun onClickResponse(home: HomeModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_recycler_view, container, false)
        val context = view.context
        mMainViewModel?.homes?.observe(this.viewLifecycleOwner, this::initList)
        configureViewModel()
        recyclerView = view as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        configureOnClickRecyclerView()
        viewModel!!.moneyType.observe(this.viewLifecycleOwner, this::changeMoney)

        return view
    }

    //update the list
    private fun initList(homes: List<HomeModel>) {
        Log.e("list", ""+homes.size)
        recyclerView!!.adapter = HomeRecyclerViewAdapter(homes)
    }

    // 1 - Configure item click on RecyclerView
    private fun configureOnClickRecyclerView() {
        var callback: Callbacks = activity as Callbacks
        ItemClickSupport.addTo(recyclerView!!, R.layout.home_list_item)
            .setOnItemClickListener { _, position, _ ->
                callback.onClickResponse(homes[position])
            }
    }

    private fun changeMoney(money: ViewModel.MoneyType) {
        initList(homes)
    }

    private fun configureViewModel(){
    val viewModelFactory:ViewModelFactory = Injection.provideViewModelFactory(context)
    this.mainViewModel =ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }
}