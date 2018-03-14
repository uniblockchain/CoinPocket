package com.vigneshwar.coinpocket.Adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.vigneshwar.coinpocket.Common.Common
import com.vigneshwar.coinpocket.Interface.ILoadMore
import com.vigneshwar.coinpocket.Model.CoinModel
import com.vigneshwar.coinpocket.R
import kotlinx.android.synthetic.main.coin_layout.view.*
import java.lang.StringBuilder

/**
 * Created by Vigneshwar on 3/10/2018.
 */
class CoinViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var coinIcon = itemView.coinIcon
    var coinSymbol = itemView.coinSymbol
    var coinName = itemView.coinName
    var coinPrice = itemView.priceUsd
    var oneHourChange = itemView.oneHour
    var twentyFourChange = itemView.twentyFourHour
    var sevenDayChange = itemView.sevenDay
}
class CoinAdapter(recyclerView: RecyclerView,internal var activity: Activity,var items:List<CoinModel>): RecyclerView.Adapter<CoinViewHolder>() {
    internal var loadMore:ILoadMore?=null
    var isLoading:Boolean=false
    var visibleThreshold=5
    var lastVisibleItem:Int=0
    var totalItemCount:Int=0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                if(!isLoading && totalItemCount <= lastVisibleItem+visibleThreshold){
                    if(loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }
            }
        })
    }
    fun setLoadMore(loadMore:ILoadMore){
        this.loadMore = loadMore
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CoinViewHolder {
        val   view = LayoutInflater.from(activity)
                .inflate(R.layout.coin_layout,parent,false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder?, position: Int){
        val coinModel = items.get(position)
        val item = holder as CoinViewHolder
        item.coinName.text = coinModel.name
        item.coinSymbol.text = coinModel.symbol
        item.coinPrice.text = coinModel.price_usd
        item.oneHourChange.text = coinModel.percent_change_1h+"%"
        item.twentyFourChange.text = coinModel.percent_change_24h+"%"
        item.sevenDayChange.text = coinModel.percent_change_7d+"%"
        Picasso.with(activity.baseContext)
                .load(StringBuilder(Common.imageUrl)
                        .append(coinModel.symbol!!.toLowerCase())
                        .append(".png")
                        .toString())
                .into(item.coinIcon)
        //Set Color
       // item.oneHourChange.setTextColor(Color.parseColor("FF0000"))
        //item.twentyFourChange.setTextColor(Color.parseColor("FF0000"))
        //item.sevenDayChange.setTextColor(Color.parseColor("FF0000"))
    }

   override fun getItemCount(): Int {
        return items.size
    }
fun setLoaded(){
    isLoading = false
}
fun updateData(coinModels: List<CoinModel>){
    this.items = coinModels
    notifyDataSetChanged()
}
}