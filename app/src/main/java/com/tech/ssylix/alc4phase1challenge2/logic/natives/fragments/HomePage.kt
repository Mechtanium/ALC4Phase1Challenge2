package com.tech.ssylix.alc4phase1challenge2.logic.natives.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.tech.ssylix.alc4phase1challenge2.R
import com.tech.ssylix.alc4phase1challenge2.data.models.Deal
import com.tech.ssylix.alc4phase1challenge2.logic.natives.activities.MainActivity
import com.tech.ssylix.alc4phase1challenge2.logic.utilities.FirebaseUtils
import com.tech.ssylix.alc4phase1challenge2.logic.viewmodel.MainViewModel
import com.tech.ssylix.alc4phase1challenge2.presenter.DefaultRecyclerAdapter
import com.tech.ssylix.alc4phase1challenge2.presenter.debugLog
import com.tech.ssylix.alc4phase1challenge2.presenter.toast
import kotlinx.android.synthetic.main.fragment_home_page.view.*
import kotlinx.android.synthetic.main.model_deal_recycler.view.*

class HomePage : Fragment() {

    val mViewModel : MainViewModel by lazy {
        (activity as MainActivity).mViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onResume() {
        super.onResume()
        "Seeing things".debugLog()
        view?.deal_recycler!!.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = DealRecyclerAdapter(mViewModel.mDealList)
            mViewModel.mDealList.size.debugLog()
        }
    }

    inner class DealRecyclerAdapter(val dealList : ArrayList<Deal> = ArrayList()) : RecyclerView.Adapter<DealRecyclerAdapter.MyViewHolder>(){

        init {
            if(FirebaseUtils.mFirebaseAuth.currentUser != null) {
                mViewModel.getDeals(this)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(LayoutInflater.from(context!!).inflate(R.layout.model_deal_recycler, parent, false))
        }

        override fun getItemCount(): Int {
            return mViewModel.mDealList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            activateGlide(holder, dealList[position].imageUrl)
            holder.itemView.destination.text = dealList[position].destination
            holder.itemView.cost.text = dealList[position].cost
            holder.itemView.accomodation.text = dealList[position].accommodation
        }

        private fun activateGlide(holder: MyViewHolder, thumbLink: String?) {
            //holder.itemView.glide_load_progress.visibility = View.VISIBLE
            Glide
                .with(context!!)
                .load(thumbLink)
                .apply {
                    addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            //holder.itemView.glide_load_progress.visibility = View.GONE
                            context?.toast("Failed")
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            //holder.itemView.glide_load_progress.visibility = View.GONE
                            return false
                        }
                    })
                }
                .apply(RequestOptions().placeholder(R.drawable.googleg_standard_color_18))
                .into(holder.itemView.image)

            /*if (holder.itemView.glide_imgV.visibility == View.VISIBLE) {
                holder.itemView.glide_imgV.visibility = View.GONE
            } else {
                holder.itemView.glide_imgV.visibility = View.VISIBLE
            }*/
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

}
