package com.contactlist.ui.list

import io.reactivex.disposables.CompositeDisposable
import android.support.v7.app.AppCompatActivity
import com.contactlist.model.SimpleContact
import com.contactlist.model.getContacts
import io.reactivex.android.schedulers.AndroidSchedulers


class ListActivityPresenter() : ListActivityMVP.Presenter {

    private lateinit var view: ListActivityMVP.View
    private var subscription = CompositeDisposable()
    private lateinit var activityRef: AppCompatActivity
    private var contactModelArrayList: ArrayList<SimpleContact>? = null


    override fun setView(view: ListActivityMVP.View, activity: AppCompatActivity) {

        this.view = view
        this.activityRef = activity
        contactModelArrayList = ArrayList()
        view.showProgress(true)

    }

    override fun loadData() {

        val normalizedNumbers = HashSet<String>()

        getContacts(activityRef.contentResolver).observeOn(AndroidSchedulers.mainThread()).subscribe {

            for (contact in it) {

                var phoneNumber = contact.phoneNumber
                var displayName = contact.displayName

                val regex = """(^(0|[+]91)+(?!${'$'}))|((\s+)|-)""".toRegex()  // check first 0,+91, (-) , 8510977618
                phoneNumber = regex.replace(phoneNumber, "")

                if (normalizedNumbers.add(phoneNumber) == true) {

                    val contact = SimpleContact(displayName, phoneNumber)
                    contactModelArrayList!!.add(contact)
                }
            }

            view.updateData(contactModelArrayList!!)
            view.showProgress(false)
            view.hideMessage()

        }

    }


}