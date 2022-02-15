package com.openclassrooms.realestatemanager.mainActivity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.addActivity.AddActivity
import com.openclassrooms.realestatemanager.detailActivity.DetailActivity
import com.openclassrooms.realestatemanager.detailActivity.fragment.DetailFragment
import com.openclassrooms.realestatemanager.editActivity.EditActivity
import com.openclassrooms.realestatemanager.mainActivity.fragment.RecyclerViewFragment
import com.openclassrooms.realestatemanager.mapActivity.MapActivity
import com.openclassrooms.realestatemanager.model.HomeModel
import com.openclassrooms.realestatemanager.model.LocationModel
import com.openclassrooms.realestatemanager.model.PhotoModel
import com.openclassrooms.realestatemanager.searchActivity.SearchActivity
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.ViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.DataViewModel
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.Injection
import com.openclassrooms.realestatemanager.viewModel.dataViewModel.ViewModelFactory
import pub.devrel.easypermissions.EasyPermissions

import androidx.annotation.NonNull
import com.openclassrooms.realestatemanager.domain.firebaseManager.PhotoManager
import com.openclassrooms.realestatemanager.domain.firebaseRepository.PhotoRepository
import pub.devrel.easypermissions.AfterPermissionGranted


class MainActivity : AppCompatActivity(), RecyclerViewFragment.Callbacks {
    var dataViewModel: DataViewModel? = null
    var mainFragment: RecyclerViewFragment? = null
    var detailFragment: DetailFragment? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    private lateinit var drawerLayout: DrawerLayout
    private val viewModel: ViewModel? = ViewModel.getInstance()
    private var navigationView: NavigationView? = null
    private val PERMS: String = Manifest.permission.READ_EXTERNAL_STORAGE
    private val RC_IMAGE_PERMS = 100
    private val RC_CHOOSE_PHOTO = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFile()
        viewModel?.moneyType?.value = ViewModel.MoneyType.DOLLAR
        configureViewModel()
        configureAndShowMainFragment()
        configureAndShowDetailFragment()
        configureToolbar()
        configureDrawerLayout()
        onDrawerOpened()
        configureNavigationView()
        dataViewModel?.allLocations?.observe(this, this::compareLocations)
    }

    private fun compareLocations(list: List<LocationModel>) {
        viewModel?.getLocationsFromFireStore(this, dataViewModel!!, list)
    }

    private fun comparePhoto(list: List<PhotoModel>) {
        dataViewModel?.let { viewModel?.getPhotosFromFireStore(this, it, list) }
        viewModel?.listPhoto?.value = list as MutableList<PhotoModel>
    }

    private fun configureToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(100)
    private fun addFile() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(
                this,"title_permission_files_access",
                RC_IMAGE_PERMS,
                PERMS
            )
            return
        }
        dataViewModel?.homes?.observe(this, this::compare)
        Toast.makeText(this, "Thank you for your permission!", Toast.LENGTH_SHORT).show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        if (this.findViewById<FrameLayout>(R.id.frameLayoutDetail) != null) {
            inflater.inflate(R.menu.menu_toolbar_600dp, menu)
            val edit: MenuItem? = menu?.findItem(R.id.edit)
            edit!!.setOnMenuItemClickListener {
                //start edit activity
                editIntent()
                return@setOnMenuItemClickListener true
            }
        } else {
            inflater.inflate(R.menu.menu_toolbar_main, menu)
        }
        val searchItem = menu!!.findItem(R.id.search)
        searchItem!!.setOnMenuItemClickListener {
            searchIntent()
            return@setOnMenuItemClickListener true
        }
        val conversion: MenuItem? = menu.findItem(R.id.convert)
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
        val addItem = menu.findItem(R.id.refreshMenu)
        addItem.setOnMenuItemClickListener {
            addIntent()
            return@setOnMenuItemClickListener true
        }
        return true
    }

    private fun editIntent() {
        val editIntent: Intent = Intent(this, EditActivity::class.java)
        startActivity(editIntent)
    }

    private fun searchIntent() {
        val searchIntent: Intent = Intent(this, SearchActivity::class.java)
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

    private fun configureNavigationView() {
        navigationView = findViewById(R.id.mainNavView)
        navigationView!!.setNavigationItemSelectedListener { item: MenuItem? ->
            onNavigationSelected(
                item!!
            )
        }
    }

    //menu
    private fun onNavigationSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mapMenu -> {
                mapIntent()
            }
            R.id.refreshMenu -> {
                refresh()
            }
        }
        this.drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }

    private fun mapIntent() {
        val menuIntent: Intent? = Intent(this, MapActivity::class.java)
        viewModel?.home = MutableLiveData<HomeModel>()
        startActivity(menuIntent)
    }

    private fun addIntent() {
        val addIntent: Intent = Intent(this, AddActivity::class.java)
        startActivity(addIntent)
    }

    private fun refresh() {
        if (Utils.isConnected(this)) {
            Toast.makeText(this, "Data refreshed!", Toast.LENGTH_SHORT).show()
            dataViewModel?.homes?.observe(this, this::compare)
        } else {
            Toast.makeText(this, "No internet available!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickResponse(home: HomeModel) {
        dataViewModel?.getPhotos(home.uid)?.observe(this, this::comparePhoto)
        viewModel!!.home.value = home
        if (detailFragment == null && this.findViewById<FrameLayout>(R.id.frameLayoutDetail) == null) {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configureViewModel() {
        val viewModelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        this.dataViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)
    }

    private fun compare(list: MutableList<HomeModel>) {
        dataViewModel?.let { viewModel?.getHomesFromFireStore(this, dataViewModel!!, list) }
    }
}