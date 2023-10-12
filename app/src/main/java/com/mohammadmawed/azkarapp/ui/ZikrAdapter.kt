package com.mohammadmawed.azkarapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohammadmawed.azkarapp.R
import com.mohammadmawed.azkarapp.data.Zikr


class ZikrAdapter(private val dataList: List<Zikr>) :
    RecyclerView.Adapter<ZikrAdapter.zikrRecViewHolder>() {

    class zikrRecViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var zikrTextViewRec: TextView = itemView.findViewById(R.id.zikrTextViewRec)
        var hintTextViewRec: TextView = itemView.findViewById(R.id.hintTextViewRec)
        var indexTextViewRec: TextView = itemView.findViewById(R.id.indexTextViewRec)
        var repeatTextViewRec: TextView = itemView.findViewById(R.id.repeatTextViewRec)
        var shareButtonContainer: RelativeLayout = itemView.findViewById(R.id.shareButtonContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): zikrRecViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_zikr, parent,
            false
        )
        return zikrRecViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: zikrRecViewHolder, position: Int) {
        val data: Zikr = dataList[position]


        val index = data.id
        val repeat = data.repeat
        holder.zikrTextViewRec.text = data.text
        holder.hintTextViewRec.text = data.hint
        holder.repeatTextViewRec.text = "$repeat" + "X"
        val aaa = index - 60
        holder.indexTextViewRec.text = "$aaa/9"

        holder.shareButtonContainer.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, (holder.zikrTextViewRec.text))
            intent.type = "text/plain"
            it.context.startActivity(Intent.createChooser(intent, "Send To"))
        }

        if (data.hint.isEmpty()) {
            holder.hintTextViewRec.visibility = TextView.GONE;
        }
        /*holder.itemView.setOnClickListener {

            //dataList.clear()
            //navController = Navigation.findNavController(holder.itemView)
            //navController!!.navigate(R.id.action_mainUIFragment_to_singleItemFragment)
        }*/
        holder.shareButtonContainer.setOnClickListener {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                val text: String = holder.zikrTextViewRec.text.toString()
                putExtra(Intent.EXTRA_TEXT, "${holder.zikrTextViewRec.text}\n\n ${holder.hintTextViewRec.text}")
            }
            it.context.startActivity(Intent.createChooser(shareIntent, "Share using"))
        }


    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}