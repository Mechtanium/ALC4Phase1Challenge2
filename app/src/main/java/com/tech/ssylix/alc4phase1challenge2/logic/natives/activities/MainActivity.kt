package com.tech.ssylix.alc4phase1challenge2.logic.natives.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.tech.ssylix.alc4phase1challenge2.R
import com.tech.ssylix.alc4phase1challenge2.logic.natives.fragments.NewDestination
import com.tech.ssylix.alc4phase1challenge2.logic.viewmodel.MainViewModel
import com.tech.ssylix.alc4phase1challenge2.presenter.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewDestination.OnFragmentInteractionListener, MainViewModel.OnViewModelLoadListener {

    override fun onIsAdminChecked(isAdmin: Boolean) {
        toggleVisibility.invoke(fab, isAdmin)
    }

    val mViewModel : MainViewModel by lazy {
        ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    private val toggleVisibility = { view : View , visible : Boolean ->
        if(visible) {
            view.visibility = View.VISIBLE
        }else{
            view.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Navigation.setViewNavController(fab, findNavController(R.id.fragment))
        fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homePage_to_newDestination))

        findNavController(R.id.fragment).addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.newDestination -> {
                    toggleVisibility.invoke(fab, false)
                }

                else -> {
                    if(mViewModel.mUserInfo?.isAdmin == true){
                        toggleVisibility.invoke(fab, true)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.init(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_admin_settings -> {
                if(mViewModel.mUserInfo != null){
                    item.isChecked = !item.isChecked
                    mViewModel.mFirebaseUtils.createUserInfo(item.isChecked, mViewModel)
                }else{
                    toast("Wait for list to load")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val AUTH_ID = 1001
    }
}
