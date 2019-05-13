package com.mapache.coinapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.mapache.coinapi.utilities.AppConstants
import kotlinx.android.synthetic.main.activity_coin.*
import kotlinx.android.synthetic.main.nav_header_main.*

class CoinActivity : AppCompatActivity() {

    var sName: String = ""
    var sCountry: String = ""
    var sValue: String = ""
    var sValueUs: String = ""
    var sYear: String = ""
    var sIsAvailable: String = ""
    var sImg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        stratData()
        bindView()
    }

    fun stratData(){
        var cIntent = intent
        sName = cIntent.getStringExtra(AppConstants.NAME_KEY)
        sCountry = cIntent.getStringExtra(AppConstants.COUNTRY_KEY)
        sValue = cIntent.getStringExtra(AppConstants.VALUE_KEY)
        sValueUs = cIntent.getStringExtra(AppConstants.VALUE_US_KEY)
        sYear = cIntent.getStringExtra(AppConstants.YEAR_KEY)
        sIsAvailable = cIntent.getStringExtra(AppConstants.IS_AVAILABLE_KEY)
        sImg = cIntent.getStringExtra(AppConstants.IMG_KEY)
    }

    fun bindView(){
        text_name_coin.text = sName
        text_country_coin.text = sCountry
        text_value_coin.text = sValue
        text_value_us_coin.text = sValueUs
        text_year_coin.text = sYear
        text_available_coin.text = sIsAvailable
        Glide.with(this).load(sImg).into(imageView)

    }

}
