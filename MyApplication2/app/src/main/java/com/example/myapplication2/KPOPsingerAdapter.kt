package com.example.myapplication2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KPOPsingerAdapter: RecyclerView.Adapter<KPOPsingerAdapter.KPOPsingerViewHolder>() {
    val TAG = "KPOPsingerAdapter"

    val data = ArrayList<KPOPsinger>()

    inner class KPOPsingerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.avatar)
        val nam = view.findViewById<TextView>(R.id.name)
        val age = view.findViewById<TextView>(R.id.age)
        val des = view.findViewById<TextView>(R.id.desc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KPOPsingerViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_kpopsinger, parent,false)

        return KPOPsingerViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: KPOPsingerViewHolder, position: Int) {
        val item = data[position]

        Log.d(TAG, "on item render : $position")

        holder.img.setImageResource(item.avatar)
        holder.nam.text = item.name
        holder.age.text = item.age.toString() + "岁"
        holder.des.text = item.desc

    }

    //设置渲染的数据
    fun setData(arr: ArrayList<KPOPsinger>){
        data.clear()
        data.addAll(arr)

        // 通知数据变化
        //notifyDataSetChanged()
    }

}