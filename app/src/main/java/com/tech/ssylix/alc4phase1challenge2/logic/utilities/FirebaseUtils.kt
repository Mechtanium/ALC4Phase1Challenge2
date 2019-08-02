package com.tech.ssylix.alc4phase1challenge2.logic.utilities

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.tech.ssylix.alc4phase1challenge2.data.UserInfo
import com.tech.ssylix.alc4phase1challenge2.data.models.Deal
import com.tech.ssylix.alc4phase1challenge2.logic.natives.fragments.HomePage
import com.tech.ssylix.alc4phase1challenge2.logic.utilities.helpers.FirebaseDatabasePathHelper
import com.tech.ssylix.alc4phase1challenge2.logic.utilities.helpers.FirebaseStoragePathHelper
import com.tech.ssylix.alc4phase1challenge2.logic.viewmodel.MainViewModel
import com.tech.ssylix.alc4phase1challenge2.presenter.toast

class FirebaseUtils {

    private val mFirebaseDatabase = FirebaseDatabase.getInstance()
    val mDatabaseReference = mFirebaseDatabase.reference
    private val mFirebaseStorage = FirebaseStorage.getInstance()
    val mStorageReference = mFirebaseStorage.reference

    var progressMinVal = 0.toLong()

    private val mFirebaseDatabasePaths by lazy {
        FirebaseDatabasePathHelper(mFirebaseAuth.uid!!)
    }

    private val mFirebaseStoragePaths by lazy {
        FirebaseStoragePathHelper()
    }

    fun beginUserLogin(activity: Activity, INT_TAG: Int) {
        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    arrayListOf(
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.EmailBuilder().build()
                    )
                )
                //.setIsSmartLockEnabled(false)
                .build(), INT_TAG
        )
    }

    fun getUserInfoRefernce() : DatabaseReference {
        return mDatabaseReference.child(mFirebaseDatabasePaths.sUserInfo)
    }

    fun createUserInfo(isAdmin : Boolean, viewModel: MainViewModel) {
        getUserInfoRefernce().setValue(UserInfo(isAdmin)).addOnSuccessListener {
            viewModel.mUserInfo = UserInfo(isAdmin)
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    fun loadUserInfo(mainViewModel: MainViewModel) {
        getUserInfoRefernce().addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                mainViewModel.mUserInfo = p0.getValue(UserInfo::class.java)
                if(mainViewModel.mUserInfo == null){
                    createUserInfo(false, mainViewModel)
                }else{
                    mainViewModel.mActivity.onIsAdminChecked(mainViewModel.mUserInfo?.isAdmin!!)
                }
            }
        })
    }

    fun getDealListReference() : DatabaseReference{
        return mDatabaseReference.child(mFirebaseDatabasePaths.sDealList)
    }

    fun getImageStore() : StorageReference{
        return  mStorageReference.child(mFirebaseStoragePaths.mDealImage)
    }

    fun uploadNewDeal(
        deal: Deal,
        imageUri: Uri,
        progressbar : ProgressBar,
        successAction: (() -> Unit)? = null
    ) {
        getImageStore().apply {
            progressMinVal = 0
            progressbar.visibility = View.VISIBLE
            putFile(imageUri).addOnSuccessListener {
                this.downloadUrl.addOnSuccessListener { uri ->
                    deal.imageUrl = uri.toString()
                    progressbar.progress = 0
                    progressbar.visibility = View.INVISIBLE
                    getDealListReference().push().setValue(deal).addOnSuccessListener {
                        successAction?.invoke()
                    }.addOnFailureListener {
                        progressbar.context.toast("Failed to save deal")
                    }
                }
            }.addOnProgressListener {
                progressbar.progressIncrementHelper(it)
            }.addOnFailureListener {
                progressbar.visibility = View.INVISIBLE
            }
        }
    }

    fun ProgressBar.progressIncrementHelper(task : UploadTask.TaskSnapshot) {
        if (progressMinVal > task.bytesTransferred && task.bytesTransferred != 0.toLong()) {
            progressMinVal = task.bytesTransferred
        }

        if (task.totalByteCount <= progressMinVal) {
            val tT = (task.totalByteCount - progressMinVal) / 100
            Thread {
                Thread.sleep(500)
                val pG = (task.totalByteCount - task.bytesTransferred) / tT
                this.isIndeterminate = false
                this.max = 100
                this.progress = 100 - pG.toInt()
            }
        } else {
            val tT = (task.totalByteCount - progressMinVal) / 100
            val pG = (task.totalByteCount - task.bytesTransferred) / tT
            this.isIndeterminate = false
            this.max = 100
            this.progress = 100 - pG.toInt()
        }
    }

    companion object {
        val mFirebaseAuth = FirebaseAuth.getInstance()
    }

    fun getDealList(recyclerAdapter: HomePage.DealRecyclerAdapter) {
        getDealListReference().addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val deal = p0.getValue(Deal::class.java)
                if(deal != null){
                    recyclerAdapter.dealList.add(deal)
                    recyclerAdapter.notifyItemInserted(recyclerAdapter.dealList.size - 1)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val deal = p0.getValue(Deal::class.java)
                if(deal != null){
                    recyclerAdapter.dealList.indexOf(recyclerAdapter.dealList.find { it == deal }).apply {
                        recyclerAdapter.dealList.removeAt(this)
                        recyclerAdapter.notifyItemRemoved(this)
                    }
                }
            }

        })
    }
}