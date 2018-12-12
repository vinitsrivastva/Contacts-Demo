package com.contactlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.FrameLayout
import com.contactlist.R

abstract class BaseActivity : AppCompatActivity() {

    private var contentFrame: FrameLayout? = null
    private var toolbar: Toolbar? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)

        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        contentFrame = findViewById(R.id.activity_content) as FrameLayout
    }

    override fun setContentView(layoutResId: Int) {
        layoutInflater.inflate(layoutResId, contentFrame, true)
    }

    protected override fun onResume() {
        super.onResume()
    }

    protected override fun onPause() {
        super.onPause()
    }

    protected override fun onDestroy() {
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}