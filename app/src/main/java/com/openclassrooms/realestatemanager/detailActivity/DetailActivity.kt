package com.openclassrooms.realestatemanager.detailActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.detailActivity.fragment.DetailFragment
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.viewModel.ViewModel

class DetailActivity : AppCompatActivity() {
    var detailFragment: DetailFragment? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    lateinit var home: HomeModel
    private val viewModel: ViewModel? = ViewModel.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        configureToolbar()
        configureAndShowDetailFragment()
    }

    private fun configureToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun configureAndShowDetailFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayoutDetail) as DetailFragment?
        if (detailFragment == null) {
            // B - Create new main fragment
            detailFragment = DetailFragment()
            // C - Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(R.id.frameLayoutDetail, detailFragment!!)
                .commit()
        }
    }
}