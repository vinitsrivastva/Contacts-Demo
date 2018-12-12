package com.contactlist.ui.list

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.contactlist.adapter.ListAdapter
import com.contactlist.main.App

import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject
import android.provider.Settings
import com.contactlist.*
import com.contactlist.model.SimpleContact


/**
 * This Fragment requests permissions to access the contacts
 * if permission is granted , it will load the contacts by
 * removing duplicates. if permission is denied , a short rationale
 * will appear to request for the permission again. if don't ask again is checked , it will show long rationale with action to
 * approve permission from app settings window
 */


class ItemListFragment: BaseFragment(), ListActivityMVP.View , ActivityCompat.OnRequestPermissionsResultCallback{

    @Inject lateinit var presenter: ListActivityMVP.Presenter

    private lateinit var rootView: View
    val data = HashMap<String, String>()
    lateinit var listAdapter: ListAdapter
    var contactList = mutableListOf<SimpleContact>()
    private val READ_CONTACTS_PERMISSION_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.getApplication() as App)
                .getAppComponent()!!
                .itemListComponent(ListActivityModule(this))
                .inject(this)

        listAdapter = ListAdapter(requireContext() , contactList , this , this )

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this ,activity as AppCompatActivity)
        initView()
    }

    private fun initView(){

        recyclerView.addItemDecoration(DividerItemDecoration(activity , DividerItemDecoration.VERTICAL))
        recyclerView.setLayoutManager(LinearLayoutManager(activity))
        recyclerView.adapter = listAdapter

        grant_permission_button.setOnClickListener {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                        READ_CONTACTS_PERMISSION_REQUEST)
            } else {

                 goToSettings()

            }
        }

    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }

     fun requestPermission() {

         // Check if the Contact permission has been granted
        if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is missing and must be requested.
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                    READ_CONTACTS_PERMISSION_REQUEST)

        } else {

            // Permission is already available, load contacts
            presenter.loadData()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

             if (requestCode === READ_CONTACTS_PERMISSION_REQUEST) {

                 // Request for contact permission.

                if (grantResults.size === 1 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                    // Permission has been granted. load contacts.

                    presenter.loadData()

                } else {

                    // Permission request was denied.
                    // Provide an additional rationale to the user if the permission was not granted
                    // and the user would benefit from additional context for the use of the permission.
                    // showRationale = false if user clicks Never Ask Again, otherwise true

                    val showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)

                    if (showRationale)
                        showMessage(context!!.resources.getString(R.string.permission_denied_rationale_short))
                     else
                        showMessage(context!!.resources.getString(R.string.permission_denied_rationale_long))

                }
            } else {

                   super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
    }

    override fun updateData(contactsData: ArrayList<SimpleContact>) {

        contactList.addAll(contactsData)
        listAdapter.notifyDataSetChanged()
    }


    override fun showProgress(show: Boolean) {

        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE

        }
    }

    override fun showMessage(msg: String) {

        permission_denied_rationale.setText(msg)
        permission_denied_view.setVisibility(View.VISIBLE)
        progressBar.visibility = View.GONE

    }

    override fun hideMessage() {
        permission_denied_view.setVisibility(View.INVISIBLE)
    }

    private fun goToSettings() {
        val uri = Uri.fromParts("package", context!!.getPackageName(), null)
        val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(settingsIntent)
    }

}
