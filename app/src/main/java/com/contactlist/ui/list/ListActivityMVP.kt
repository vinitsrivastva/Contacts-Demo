package com.contactlist.ui.list

import android.support.v7.app.AppCompatActivity
import com.contactlist.model.SimpleContact
import io.reactivex.Observable


public interface ListActivityMVP {

    interface View{
        fun updateData(contactsData: ArrayList<SimpleContact>)
        fun showProgress(show: Boolean)
        fun showMessage(msg: String)
        fun hideMessage()
    }

    interface Presenter{

        fun setView(view: View , activity:AppCompatActivity)
        fun loadData()
    }

    interface Model {

        // intentionally left blank
    }

}
