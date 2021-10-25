package cn.ddh.simulatedclick.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.ddh.simulatedclick.R
import cn.ddh.simulatedclick.event.EventBase

class EventAdapter(private val context: Context, private val list: MutableList<EventBase>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.tv_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.item_event, null)
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventBase = list[position]
        holder.textView.text = eventBase.getName()
        holder.textView.setTextColor(context.resources.getColor(if (eventBase.getTasking()) R.color.teal_200 else R.color.black))
    }
}