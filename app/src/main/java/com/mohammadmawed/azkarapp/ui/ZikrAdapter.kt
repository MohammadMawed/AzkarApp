package com.mohammadmawed.azkarapp.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr

class ZikrAdapter(private val dataList: List<Zikr>): RecyclerView.Adapter<ZikrAdapter.mViewHolder>() {

    class mViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var zikrTextViewRec: TextView = itemView.findViewById(R.id.zikrTextViewRec)
        var hintTextViewRec: TextView = itemView.findViewById(R.id.hintTextViewRec)
        var indexTextViewRec: TextView = itemView.findViewById(R.id.indexTextViewRec)
        var repeatTextViewRec: TextView = itemView.findViewById(R.id.repeatTextViewRec)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_zikr, parent,
            false
        )
        return mViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val data: Zikr = dataList[position]


        holder.zikrTextViewRec.text = data.text
        holder.hintTextViewRec.text = data.hint
        val index = data.id
        val repeat = data.repeat
        holder.repeatTextViewRec.text = "$repeat"+"X"
        holder.indexTextViewRec.text = "$index/60"

        /*holder.itemView.setOnClickListener {

            //dataList.clear()
            //navController = Navigation.findNavController(holder.itemView)
            //navController!!.navigate(R.id.action_mainUIFragment_to_singleItemFragment)
        }*/
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}