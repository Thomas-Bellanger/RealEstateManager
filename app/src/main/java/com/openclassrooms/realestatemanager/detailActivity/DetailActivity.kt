package com.openclassrooms.realestatemanager.detailActivity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.detailActivity.fragment.DetailFragment
import com.openclassrooms.realestatemanager.editActivity.EditActivity
import com.openclassrooms.realestatemanager.viewModel.ViewModel

class DetailActivity : AppCompatActivity() {
    var detailFragment: DetailFragment? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    private val viewModel: ViewModel? = ViewModel.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        configureToolbar()
        configureAndShowDetailFragment()
    }

    //toolbar
    private fun configureToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    //detail fragment
    private fun configureAndShowDetailFragment() {
        detailFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayoutDetail) as DetailFragment?
        detailFragment = DetailFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayoutDetail, detailFragment!!)
            .commit()
    }

    //menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar_detail, menu)
        var edit: MenuItem? = menu?.findItem(R.id.edit)
        edit!!.setOnMenuItemClickListener {
            //start edit activity
            editIntent()
            return@setOnMenuItemClickListener true
        }
        //conversion money
        var conversion: MenuItem? = menu?.findItem(R.id.convert)
        conversion!!.setOnMenuItemClickListener {
            if (viewModel != null) {
                if (viewModel.moneyType.value == ViewModel.MoneyType.DOLLAR) {
                    viewModel.moneyType.value = ViewModel.MoneyType.EURO
                    conversion.icon = getDrawable(R.drawable.baseline_euro_symbol_yellow_700_24dp)
                } else if (viewModel.moneyType.value == ViewModel.MoneyType.EURO) {
                    viewModel.moneyType.value = ViewModel.MoneyType.DOLLAR
                    conversion.icon = getDrawable(R.drawable.baseline_attach_money_green_a700_24dp)
                }
            }
            if (viewModel != null) {
            }
            return@setOnMenuItemClickListener true
        }
        return true
    }

    //edit activity intent
    private fun editIntent() {
        var editIntent: Intent? = Intent(this, EditActivity::class.java)
        startActivity(editIntent)
    }
}