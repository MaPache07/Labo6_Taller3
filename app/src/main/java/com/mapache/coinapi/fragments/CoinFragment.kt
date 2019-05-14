package com.mapache.coinapi.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mapache.coinapi.R
import com.mapache.coinapi.models.Coin
import com.mapache.coinapi.utilities.AppConstants
import kotlinx.android.synthetic.main.content_fragment.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class CoinFragment : Fragment(){

    lateinit var coin : Coin

    companion object{
        fun newInstance(coin : Coin) : CoinFragment {
            val newFragment = CoinFragment()
            newFragment.coin = coin
            return newFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.content_fragment, container, false)
        if(savedInstanceState != null) coin = savedInstanceState.getParcelable(AppConstants.COIN_KEY)
        bindView(view)
        return view
    }

    fun bindView(view : View){
        view.text_name_coin.text = coin.name
        view.text_country_coin.text = coin.country
        view.text_value_coin.text = coin.value.toString()
        view.text_value_us_coin.text = coin.values_us.toString()
        view.text_year_coin.text = coin.year.toString()
        view.text_available_coin.text = coin.isAvailable.toString()
        Glide.with(view).load(coin.img).into(view.coin_image)
        Log.d("Hola", coin.country)
        when(coin.country){
            "Australia" -> view.nav_bar.setBackgroundResource(R.drawable.australia)
            "Suiza" -> view.nav_bar.setBackgroundResource(R.drawable.suiza)
            "Mexico" -> view.nav_bar.setBackgroundResource(R.drawable.mexico)
            "Europa" -> view.nav_bar.setBackgroundResource(R.drawable.europa)
            "Canada" -> view.nav_bar.setBackgroundResource(R.drawable.canada)
            "Japon" -> view.nav_bar.setBackgroundResource(R.drawable.japon)
            "Venezuela" -> view.nav_bar.setBackgroundResource(R.drawable.venezuela)
            "El salvador" ->  view.nav_bar.setBackgroundResource(R.drawable.el_salvador)
            "Inglaterra" -> view.nav_bar.setBackgroundResource(R.drawable.inglaterra)
            "Corea del sur" -> view.nav_bar.setBackgroundResource(R.drawable.corea_sur)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(AppConstants.COIN_KEY, coin)
    }
}