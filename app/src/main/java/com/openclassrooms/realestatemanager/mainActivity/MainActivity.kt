package com.openclassrooms.realestatemanager.mainActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.addActivity.AddActivity
import com.openclassrooms.realestatemanager.detailActivity.DetailActivity
import com.openclassrooms.realestatemanager.detailActivity.fragment.DetailFragment
import com.openclassrooms.realestatemanager.domain.manager.HomeManager
import com.openclassrooms.realestatemanager.editActivity.EditActivity
import com.openclassrooms.realestatemanager.mainActivity.fragment.RecyclerViewFragment
import com.openclassrooms.realestatemanager.mapActivity.MapActivity
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.viewModel.ViewModel


class MainActivity : AppCompatActivity(), RecyclerViewFragment.Callbacks {
    var mainFragment: RecyclerViewFragment? = null
    var detailFragment: DetailFragment? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    private lateinit var drawerLayout: DrawerLayout
    private val viewModel: ViewModel? = ViewModel.getInstance()
    private var navigationView: NavigationView? = null
    private var homeManager:HomeManager? = HomeManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeManager!!.createHomeFirebase(HomeModel.testHome)
        homeManager?.createHomeFirebase(HomeModel.testHome2)
        viewModel?.moneyType?.value = ViewModel.MoneyType.DOLLAR
        configureAndShowMainFragment()
        configureAndShowDetailFragment()
        configureToolbar()
        configureDrawerLayout()
        onDrawerOpened()
        configureNavigationView()
    }

    private fun configureToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar, menu)
        var searchItem = menu!!.findItem(R.id.search)
        searchItem!!.setOnMenuItemClickListener {
            searchIntent()
            return@setOnMenuItemClickListener true
        }

        var conversion: MenuItem? = menu.findItem(R.id.convert)
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
                Log.e("click", "clicked  " + viewModel.moneyType.value)
            }
            return@setOnMenuItemClickListener true
        }
        var edit: MenuItem? = menu.findItem(R.id.edit)
        edit!!.setOnMenuItemClickListener {
            //start edit activity
            editIntent()
            return@setOnMenuItemClickListener true
        }
        return true
    }

    private fun editIntent() {
        var editIntent: Intent? = Intent(this, EditActivity::class.java)
        startActivity(editIntent)
    }

    private fun searchIntent() {
        var searchIntent: Intent? = Intent(this, EditActivity::class.java)
        startActivity(searchIntent)
    }

    private fun configureAndShowMainFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mainFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayoutMain) as RecyclerViewFragment?
        if (mainFragment == null) {
            // B - Create new main fragment
            mainFragment = RecyclerViewFragment()
            // C - Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(R.id.frameLayoutMain, mainFragment!!)
                .commit()
        }
    }

    private fun configureAndShowDetailFragment() {
        detailFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayoutDetail) as DetailFragment?
        if (detailFragment == null && this.findViewById<FrameLayout>(R.id.frameLayoutDetail) != null) {
            detailFragment = DetailFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frameLayoutDetail, detailFragment!!)
                .commit()
        }

    }

    private fun configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.drawer_Layout)
        var toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun onDrawerOpened() {
        val navigationView = findViewById<NavigationView>(R.id.mainNavView)
        val headerView = navigationView.getHeaderView(0)
        val menu = navigationView.menu
    }

    fun configureNavigationView() {
        navigationView = findViewById(R.id.mainNavView)
        navigationView!!.setNavigationItemSelectedListener { item: MenuItem? ->
            onNavigationSelected(
                item!!
            )
        }
    }

    //menu
    private fun onNavigationSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.mapMenu -> {
                mapIntent()
            }
            R.id.addMenu -> {
                addIntent()
            }
        }
        this.drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }

    private fun mapIntent() {
        var menuIntent: Intent? = Intent(this, MapActivity::class.java)
        startActivity(menuIntent)
    }

    private fun addIntent() {
        var addIntent: Intent? = Intent(this, AddActivity::class.java)
        startActivity(addIntent)
    }

    override fun onClickResponse(home: HomeModel) {
        viewModel!!.setMyHome(home)
        if (detailFragment == null && this.findViewById<FrameLayout>(R.id.frameLayoutDetail) == null) {
            val intent = Intent(this, DetailActivity::class.java)
            Log.e("click","clicked!")
            startActivity(intent)
        }
    }
}