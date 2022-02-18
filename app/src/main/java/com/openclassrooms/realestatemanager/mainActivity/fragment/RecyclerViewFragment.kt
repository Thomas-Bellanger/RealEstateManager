package com.openclassrooms.realestatemanager.mainActivity.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.Injection
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.ViewModelFactory
import java.util.*
import javax.security.auth.callback.Callback


class RecyclerViewFragment : Fragment(), Callback {

    private var recyclerView: RecyclerView? = null
    var listHomes: List<HomeModel> = ArrayList()
    private val viewModel: ViewModel? = ViewModel.getInstance()
    var dataViewModel: DataViewModel? = null
    private var cancelBtn: Button? = null
    var selectedView: View? = null

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
        configureViewModel()
        cancelBtn = view.findViewById(R.id.cancelFilterButton)
        recyclerView = view.findViewById(R.id.homeRecyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        dataViewModel?.homes?.observe(this.viewLifecycleOwner, this::initList)
        viewModel?.listHomesFiltered?.observe(this.viewLifecycleOwner, this::filter)
        viewModel!!.moneyType.observe(this.viewLifecycleOwner, this::changeMoney)
        cancelBtn?.setOnClickListener { cancelFilter() }
        return view
    }

    //update the list
    private fun initList(homes: List<HomeModel>) {
        recyclerView!!.adapter = HomeRecyclerViewAdapter(homes)
        configureOnClickRecyclerView(homes)
        viewModel?.listHomesFull = homes as MutableList<HomeModel>
        listHomes = homes
    }

    // 1 - Configure item click on RecyclerView
    private fun configureOnClickRecyclerView(homes: List<HomeModel>) {
        val callback: Callbacks = activity as Callbacks
        if (selectedView != null) {
            selectedView?.setBackgroundColor(Color.WHITE)
        }

        ItemClickSupport.addTo(recyclerView!!, R.layout.home_list_item)
            .setOnItemClickListener { _, position, itemView ->
                callback.onClickResponse(homes[position])
                itemView?.setBackgroundColor(Color.GREEN)
                selectedView = itemView
            }
    }

    private fun changeMoney(money: ViewModel.MoneyType) {
        initList(listHomes)
    }

    private fun configureViewModel() {
        val viewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(context)
        this.dataViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)
    }

    private fun filter(homes: List<HomeModel>) {
        dataViewModel?.homes?.observe(this.viewLifecycleOwner, this::setFull)
        cancelBtn?.visibility = VISIBLE
        recyclerView!!.adapter = HomeRecyclerViewAdapter(homes)
    }

    private fun cancelFilter() {
        cancelBtn?.visibility = GONE
        dataViewModel?.homes?.observe(this.viewLifecycleOwner, this::initList)
    }

    private fun setFull(homes: List<HomeModel>) {
        if (viewModel?.listHomesFiltered?.value == homes) {
            cancelBtn?.visibility = GONE
        }
        viewModel?.listHomesFull = homes as MutableList<HomeModel>
    }

    override fun onPause() {
        super.onPause()
        selectedView?.setBackgroundColor(Color.WHITE)
    }
}