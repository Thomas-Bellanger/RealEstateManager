package com.openclassrooms.realestatemanager.mainActivity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import java.util.*
import javax.security.auth.callback.Callback


class RecyclerViewFragment : Fragment(), Callback {

    private var recyclerView: RecyclerView? = null
    var homes: MutableList<HomeModel> = ArrayList()

    interface Callbacks {
        fun onClickResponse(home: HomeModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_recycler_view, container, false)
        val context = view.context
        recyclerView = view as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        homes.add(HomeModel.testHome)
        homes.add(HomeModel.testHome2)
        initList(homes)
        configureOnClickRecyclerView()
        return view
    }

    //update the list
    private fun initList(homes: List<HomeModel>) {
        recyclerView!!.adapter = HomeRecyclerViewAdapter(homes)
    }

    // 1 - Configure item click on RecyclerView
    private fun configureOnClickRecyclerView() {
        var callback: Callbacks = activity as Callbacks
        ItemClickSupport.addTo(recyclerView!!,R.layout.home_list_item)
            .setOnItemClickListener { _, position, _ ->
                callback.onClickResponse(homes[position])
            }
    }

    companion object {
        fun newInstance(): RecyclerViewFragment {
            return newInstance()
        }
    }
}