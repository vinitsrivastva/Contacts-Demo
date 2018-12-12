package com.contactlist.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.contactlist.R
import com.contactlist.model.SimpleContact
import com.contactlist.ui.list.ListActivityMVP
import kotlinx.android.synthetic.main.item_layout.view.*

class ListAdapter(private val context: Context, private val list: List<SimpleContact>, fragment: Fragment,
                  private var itemView: ListActivityMVP.View) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var contact = list[position]

            holder.name.setText(contact.displayName)
            holder.number.setText(contact.phoneNumber)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_layout, parent, false))
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout = view.itemLayout
        val name = view.txtName
        val number = view.txtNumber
    }

    }