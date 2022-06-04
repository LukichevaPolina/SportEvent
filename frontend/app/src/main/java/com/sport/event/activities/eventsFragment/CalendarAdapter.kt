package com.sport.event.activities.eventsFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sport.event.Constants
import com.sport.event.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(private val context: Context,
                      private val data: ArrayList<Date>,
                      private val currentDate: Calendar,
                      private val changeMonth: Calendar?): RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    // will use for onCliskListener
    private var mListener: OnItemClickListener? = null
    private var index = -1

    // boolean that will select the date when we load the month after we click on another date it changes to false
    private var selectCurrentDate = true

    private val selectedDay = currentDate[Calendar.DAY_OF_MONTH]

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_calendar, parent, false)
        return CalendarViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val cal = Calendar.getInstance()
        cal.time = data[position]
        val displayDay = cal[Calendar.DAY_OF_MONTH]

        holder.dayOfWeek.text = Constants.daysOfWeek[cal[Calendar.DAY_OF_WEEK] - 1]
        holder.date.text = cal[Calendar.DAY_OF_MONTH].toString()

        holder.cardView.setOnClickListener {
            index = position
            selectCurrentDate = false
            holder.listener?.onItemClick(position)
            notifyDataSetChanged()
        }

        if (index == position) {
            makeItemSelected(holder)
        }
        else {
            if (displayDay == selectedDay
                && selectCurrentDate
            ) {
                makeItemSelected(holder)
            }
            else
                makeItemDefault(holder)
        }
    }

    override fun getItemCount(): Int = data.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    inner class CalendarViewHolder(itemView: View, val listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeek: TextView = itemView.findViewById(R.id.day_of_the_week)
        val date: TextView = itemView.findViewById(R.id.date)
        var cardView: CardView = itemView.findViewById(R.id.calendar_card)
    }

    private fun makeItemSelected(holder: CalendarViewHolder) {
        holder.date.setTextColor(ContextCompat.getColor(context, R.color.Base_0))
        holder.dayOfWeek.setTextColor(ContextCompat.getColor(context, R.color.Base_0))
        holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.AppleGreen_400))
        holder.cardView.isEnabled = false
    }

    private fun makeItemDefault(holder: CalendarViewHolder) {
        holder.date.setTextColor(ContextCompat.getColor(context, R.color.Base_500))
        holder.dayOfWeek.setTextColor(ContextCompat.getColor(context, R.color.Base_500))
        holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.Base_0))
        holder.cardView.isEnabled = true
    }
}
