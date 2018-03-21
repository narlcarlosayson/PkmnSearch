package com.sayson.narl.pkmnsearch


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pkmn_item.view.*


/**
 * Created by Lran on 3/18/2018.
 */
class AdapterTypes (val typesList: ArrayList<Types>): RecyclerView.Adapter<AdapterTypes.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.pkmn_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return typesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val types: Types = typesList[position]
        holder?.view?.txt1?.setText(types.types)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }
}