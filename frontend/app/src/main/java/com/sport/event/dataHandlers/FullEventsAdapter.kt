package com.sport.event.dataHandlers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sport.event.R
import com.sport.event.retrofit.models.Event

class FullEventsAdapter(pb: View) : RecyclerView.Adapter<FullEventsAdapter.EventsViewHolder>() {

    var events = mutableListOf<Event?>()
    var progressBar = pb

    @SuppressLint("NotifyDataSetChanged")
    fun setEventList(events:ArrayList<Event>) {
        this.events = events.toMutableList()
        progressBar.visibility = View.GONE
        notifyDataSetChanged() //Notify any registered observers that the data set has changed.
    }

    //create viewHandler object and pass layout (called by LayoutManager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_full_information_event, parent, false)

        return EventsViewHolder(view)
    }

    //connect data and viewHolder (called by LayoutManager)
    override fun onBindViewHolder(holder: EventsViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //set data to view
        val event = events[position]
        holder.sport.text = event?.sport

        val place = event?.address
        holder.place.text = place

        val time = (event?.start_time) + " - " + (event?.end_time)
        holder.textTime.text = time

        val date = (event?.date)
        holder.textDate.text = date

        val owner = (event?.owner)
        holder.textOwner.text = owner
    }

    //total item number
    override fun getItemCount(): Int = events.size

    class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sport: TextView = itemView.findViewById(R.id.sport)
        val place: TextView = itemView.findViewById(R.id.place)
        val textTime: TextView = itemView.findViewById(R.id.text_time)
        val textDate: TextView = itemView.findViewById(R.id.text_date)
        val textOwner: TextView = itemView.findViewById(R.id.text_owner)
    }
}