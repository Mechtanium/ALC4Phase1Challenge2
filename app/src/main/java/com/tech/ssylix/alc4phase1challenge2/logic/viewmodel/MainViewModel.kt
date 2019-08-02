package com.tech.ssylix.alc4phase1challenge2.logic.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.tech.ssylix.alc4phase1challenge2.data.UserInfo
import com.tech.ssylix.alc4phase1challenge2.data.models.Deal
import com.tech.ssylix.alc4phase1challenge2.logic.natives.activities.MainActivity
import com.tech.ssylix.alc4phase1challenge2.logic.natives.fragments.HomePage
import com.tech.ssylix.alc4phase1challenge2.logic.utilities.FirebaseUtils
import com.tech.ssylix.alc4phase1challenge2.presenter.toast

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val mFirebaseUtils : FirebaseUtils by lazy {
        FirebaseUtils()
    }

    var mUserInfo : UserInfo? = null

    var mDealList : ArrayList<Deal> = ArrayList()

    lateinit var mActivity: MainActivity

    fun init(activity: Activity) {
        mActivity = activity as MainActivity
        if(FirebaseUtils.mFirebaseAuth.currentUser == null){
            mActivity.toast("HACK: You need to sign to continue", Toast.LENGTH_LONG)
            mFirebaseUtils.beginUserLogin(activity, MainActivity.AUTH_ID)
        }else{
            if(mUserInfo == null) {
                mFirebaseUtils.loadUserInfo(this)
            }
        }
    }

    fun getDeals(adapter: HomePage.DealRecyclerAdapter): ArrayList<Deal> {
        return if(mDealList.isEmpty()) {
            mFirebaseUtils.getDealList(adapter)
            mDealList
        } else adapter.dealList.apply {
            clear()
            addAll(mDealList)
        }
    }

    interface OnViewModelLoadListener{

        fun onIsAdminChecked(isAdmin : Boolean)
    }
}