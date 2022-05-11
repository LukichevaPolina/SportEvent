package com.sport.event.activities.eventsFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sport.event.R
import com.sport.event.retrofit.models.Event

//set all the items into the recyclerview
class EventsAdapter: RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {
    var events = mutableListOf<Event>()

    @SuppressLint("NotifyDataSetChanged")
    fun setEventList(events:ArrayList<Event>) {
        this.events = events.toMutableList()
        notifyDataSetChanged() //Notify any registered observers that the data set has changed.
    }

    //create viewHandler object and pass layout (called by LayoutManager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_event, parent, false)
        return EventsViewHolder(view)
    }

    //connect data and viewHolder (called by LayoutManager)
    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val event = events[position]
        holder.names.text = event.sport.toString()
    }

    //total item number
    override fun getItemCount(): Int = events.size

    class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val names: TextView = itemView.findViewById(R.id.name)
    }
}