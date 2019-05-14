package com.mapache.coinapi

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.DatabaseUtils
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.provider.BaseColumns
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import com.google.gson.Gson
import com.mapache.coinapi.data.Database
import com.mapache.coinapi.data.DatabaseContract
import com.mapache.coinapi.fragments.CoinFragment
import com.mapache.coinapi.models.Coin
import com.mapache.coinapi.models.CoinList
import com.mapache.coinapi.utilities.AppConstants
import com.mapache.coinapi.utilities.NetworkUtil
import kotlinx.android.synthetic.main.content_main.*
import java.io.IOException
import kotlin.math.log

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var filterList = ArrayList<Coin>()
    var cList = ArrayList<Coin>()
    var dbHelper = Database(this)
    lateinit var coinList : CoinList
    lateinit var adapterC : CoinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        if(savedInstanceState != null){
            filterList = savedInstanceState.getParcelableArrayList(AppConstants.FILTER_KEY)
            initRecycler(filterList)
        } else FetchCoinTask().execute(AppConstants.COIN_LINK)
    }

    fun initRecycler(list : ArrayList<Coin>){
        var viewManager = LinearLayoutManager(this)
        if(this.resources.configuration.orientation == 2 || this.resources.configuration.orientation == 4){
            viewManager = LinearLayoutManager(this)
        }
        else{
            viewManager = GridLayoutManager(this, 2)
        }
        adapterC = CoinAdapter(list, {coin: Coin -> clickedCoin(coin)})
        recyclerview.apply {
            adapter = adapterC
            layoutManager = viewManager
        }
    }

    fun clickedCoin(coin: Coin){
        if(this.resources.configuration.orientation == 2 || this.resources.configuration.orientation == 4){
            var coinFragment = CoinFragment.newInstance(coin)
            supportFragmentManager.beginTransaction().replace(R.id.scroll_content, coinFragment).commit()
        }
        else{
            var bundle = Bundle()
            bundle.putParcelable(AppConstants.COIN_KEY, coin)
            var  mIntent = Intent(this, CoinActivity::class.java)
            mIntent.putExtras(bundle)
            startActivity(mIntent)
        }
    }

    fun inToBase(){
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            DatabaseContract.CoinEntry.COLUMN_NAME,
            DatabaseContract.CoinEntry.COLUMN_COUNTRY,
            DatabaseContract.CoinEntry.COLUMN_VALUE,
            DatabaseContract.CoinEntry.COLUMN_VALUE_US,
            DatabaseContract.CoinEntry.COLUMN_YEAR,
            DatabaseContract.CoinEntry.COLUMN_REVIEW,
            DatabaseContract.CoinEntry.COLUMN_ISAVAILABLE,
            DatabaseContract.CoinEntry.COLUMN_IMG
        )
        val sortOrder = "${DatabaseContract.CoinEntry.COLUMN_NAME} ASC"
        val cursor = db.query(
            DatabaseContract.CoinEntry.TABLE_NAME, // nombre de la tabla
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )
        with(cursor) {
            while (moveToNext()) {
                var coin = Coin(
                    getString(getColumnIndexOrThrow(BaseColumns._ID)),
                    getString(getColumnIndexOrThrow(DatabaseContract.CoinEntry.COLUMN_NAME)),
                    getString(getColumnIndexOrThrow(DatabaseContract.CoinEntry.COLUMN_COUNTRY)),
                    getInt(getColumnIndexOrThrow(DatabaseContract.CoinEntry.COLUMN_VALUE)),
                    getDouble(getColumnIndexOrThrow(DatabaseContract.CoinEntry.COLUMN_VALUE_US)),
                    getInt(getColumnIndexOrThrow(DatabaseContract.CoinEntry.COLUMN_YEAR)),
                    getString(getColumnIndexOrThrow(DatabaseContract.CoinEntry.COLUMN_REVIEW)),
                    getString(getColumnIndexOrThrow(DatabaseContract.CoinEntry.COLUMN_ISAVAILABLE)).toBoolean(),
                    getString(getColumnIndexOrThrow(DatabaseContract.CoinEntry.COLUMN_IMG))

                )
                cList.add(coin)
            }
        }
        initRecycler(cList)
    }

    inner class FetchCoinTask : AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg params: String?): String? {
            if(isNetworkAvailable()){
                if(params.size == 0){
                    return null
                }
                var coinApi = NetworkUtil().buildUrl(params[0]!!)
                var listCoin = ""
                try {
                    listCoin = NetworkUtil().getResponseFromHttpUrl(coinApi)
                } catch (e : IOException){
                    throw RuntimeException("No se logro obtener la informacion")
                }
                val db = dbHelper.writableDatabase
                coinList = Gson().fromJson(listCoin, CoinList::class.java)
                cList = coinList.coins
                if(DatabaseUtils.queryNumEntries(db, "coin").toInt() != coinList.coins.size){
                    db.delete("coin", null, null)
                    for(coin: Coin in cList){
                        val values = ContentValues().apply {
                            put(DatabaseContract.CoinEntry.COLUMN_ID, coin._id)
                            put(DatabaseContract.CoinEntry.COLUMN_NAME, coin.name)
                            put(DatabaseContract.CoinEntry.COLUMN_COUNTRY, coin.country)
                            put(DatabaseContract.CoinEntry.COLUMN_VALUE, coin.value)
                            put(DatabaseContract.CoinEntry.COLUMN_VALUE_US, coin.values_us)
                            put(DatabaseContract.CoinEntry.COLUMN_YEAR, coin.year)
                            put(DatabaseContract.CoinEntry.COLUMN_REVIEW, coin.review)
                            put(DatabaseContract.CoinEntry.COLUMN_ISAVAILABLE, coin.isAvailable)
                            put(DatabaseContract.CoinEntry.COLUMN_IMG, coin.img)
                        }
                        db?.insert(DatabaseContract.CoinEntry.TABLE_NAME, null, values)
                    }
                    return "0"
                }
            }
            else return "1"
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(result == "1") inToBase()
            else initRecycler(cList)
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if(filterList.size != 0){
            outState!!.putParcelableArrayList(AppConstants.FILTER_KEY, filterList)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        filterList.clear()
        when (item.itemId) {
            R.id.nav_home -> {
                FetchCoinTask().execute(AppConstants.COIN_LINK)
            }
            R.id.nav_australia -> {
                QuitCoin("Australia")
            }
            R.id.nav_canada -> {
                QuitCoin("Canada")
            }
            R.id.nav_corea_sur -> {
                QuitCoin("Corea del sur")
            }
            R.id.nav_el_salvador -> {
                QuitCoin("El salvador")
            }
            R.id.nav_europa -> {
                QuitCoin("Europa")
            }
            R.id.nav_inglaterra -> {
                QuitCoin("Inglaterra")
            }
            R.id.nav_japon -> {
                QuitCoin("Japon")
            }
            R.id.nav_mexico -> {
                QuitCoin("Mexico")
            }
            R.id.nav_suiza -> {
                QuitCoin("Suiza")
            }
            R.id.nav_venezuela -> {
                QuitCoin("Venezuela")
            }
        }
        initRecycler(filterList)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun QuitCoin(country : String){
        for(coin : Coin in cList){
            if(coin.country == country) filterList.add(coin)
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
