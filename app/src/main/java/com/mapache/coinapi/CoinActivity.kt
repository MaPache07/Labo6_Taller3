package com.mapache.coinapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mapache.coinapi.fragments.CoinFragment
import com.mapache.coinapi.models.Coin
import com.mapache.coinapi.utilities.AppConstants

class CoinActivity : AppCompatActivity() {

    lateinit var coinFragment : CoinFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        val coin : Coin? = intent?.extras?.getParcelable(AppConstants.COIN_KEY)
        coinFragment = CoinFragment.newInstance(coin!!)
        supportFragmentManager.beginTransaction().replace(R.id.coin_content_fragment, coinFragment).commit()
    }
}
