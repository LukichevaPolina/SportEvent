package com.sport.event.activities.eventsFragment

import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sport.event.Constants
import com.sport.event.R
import com.sport.event.accountManager.AccountManagerHelper
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//set all the items into the recyclerview
class EventsAdapter(pb: View): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {
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
            .inflate(R.layout.adapter_event, parent, false)

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

        //work with accountManager
        val accountManager = AccountManager.get(holder.itemView.context)
        val account = AccountManagerHelper().getAccount(accountManager)

        //handle button click
        val username: String = accountManager.getUserData(account, Constants.USERNAME)
        val userId: Int = accountManager.getUserData(account, Constants.USER_ID).toInt()

        buttonSettings(username, userId, position, holder)

        holder.button.setOnClickListener {
            if (username == events[position]?.owner) {
                holder.button.isEnabled = true
                makeItemDisabled(holder)
            } else if (events[position]?.free_seats == 0 && events[position]?.members?.contains(userId) == false) {
                holder.button.isEnabled = true
                makeItemFull(holder)
            } else {
                //update token
                val future: AccountManagerFuture<Bundle> = AccountManagerHelper().getFutureUpdateToken(accountManager)

                //unjoin from event
                if (events[position]?.members?.contains(userId) == true) {
                    Thread {
                        val authToken = future.result.getString(AccountManager.KEY_AUTHTOKEN)
                        APIApp.restClient?.service?.unjoin("Bearer " + authToken, events[position]?.id)?.enqueue(object:
                            Callback<Event> {
                            override fun onResponse(
                                call: Call<Event>,
                                response: Response<Event>
                            ) {
                                events[position] = response.body()
                                notifyItemChanged(position)
                            }

                            override fun onFailure(call: Call<Event>, t: Throwable) {
                                t.printStackTrace()
                            }
                        })
                    }.start()

                } else {
                    //join to event
                    Thread {
                        val authToken = future.result.getString(AccountManager.KEY_AUTHTOKEN)
                        APIApp.restClient?.service?.join("Bearer " + authToken, events[position]?.id)?.enqueue(object:
                            Callback<Event> {
                            override fun onResponse(
                                call: Call<Event>,
                                response: Response<Event>
                            ) {
                                events[position] = response.body()
                                notifyItemChanged(position)
                            }

                            override fun onFailure(call: Call<Event>, t: Throwable) {
                                t.printStackTrace()
                            }
                        })
                    }.start()
                }
            }
        }
        progressBar.visibility = View.GONE
    }

    //total item number
    override fun getItemCount(): Int = events.size

    class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sport: TextView = itemView.findViewById(R.id.sport)
        val place: TextView = itemView.findViewById(R.id.place)
        val textTime: TextView = itemView.findViewById(R.id.text_time)
        val button: Button = itemView.findViewById(R.id.button)
    }

    private fun buttonSettings(username: String, id: Int, position: Int, holder: EventsViewHolder) {
        if (username == events[position]?.owner) {
            holder.button.isEnabled = true
            makeItemDisabled(holder)
        } else if (events[position]?.free_seats == 0 && events[position]?.members?.contains(id) == false) {
            holder.button.isEnabled = true
            makeItemFull(holder)
        } else {
            if (events[position]?.members?.contains(id) == true) {
                makeItemJoined(holder)
            } else {
                makeItemUnjoined(holder)
            }
        }
    }

    private fun makeItemJoined(holder: EventsViewHolder) {
        holder.button.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.Orange_300
            )
        )
        holder.button.text = "Не пойду"
    }

    private fun makeItemUnjoined(holder: EventsViewHolder) {
        holder.button.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.AppleGreen_500
            )
        )
        holder.button.text = "Присоединиться"
    }

    private fun makeItemDisabled(holder: EventsViewHolder) {
        holder.button.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.Base_300
            )
        )
        holder.button.text = "Вы организатор"
    }

    private fun makeItemFull(holder: EventsViewHolder) {
        holder.button.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.Base_300
            )
        )
        holder.button.text = "Нет мест"
    }
}
