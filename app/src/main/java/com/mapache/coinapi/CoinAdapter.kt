package com.mapache.coinapi

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mapache.coinapi.models.Coin
import kotlinx.android.synthetic.main.list_item_coin.view.*

class CoinAdapter(val coins:List<Coin>, val clickedCoinListener: (Coin) -> Unit) : RecyclerView.Adapter<CoinAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CoinAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_coin, null, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = coins.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(coins[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: Coin) {
            itemView.text_coin.text = item.name
            with(itemView){
                Glide.with(itemView).load(item.img).placeholder(R.drawable.ic_launcher_background).into(itemView.img_coin)
            }
            itemView.setOnClickListener{
                clickedCoinListener(item)
            }
        }
    }
}