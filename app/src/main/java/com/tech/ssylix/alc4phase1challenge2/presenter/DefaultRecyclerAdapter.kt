package com.tech.ssylix.alc4phase1challenge2.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tech.ssylix.alc4phase1challenge2.R

class DefaultRecyclerAdapter(
    val context: Context,
    private val itemModel: Int,
    private val itemCnt: Int = 18,
    initAction: (() -> Unit)? = null,
    private val bindAction: ((holder: RecyclerView.ViewHolder, position: Int) -> Unit)? = null,
    private val clickAction: ((view: View, holder: RecyclerView.ViewHolder) -> Unit)? = null
) : RecyclerView.Adapter<DefaultRecyclerAdapter.MyViewHolder>() {

    var emptyList = false
    var DEFAULT_EMPTY = 1001

    init {
        initAction?.invoke()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when (viewType) {
            DEFAULT_EMPTY -> MyViewHolder(
                LayoutInflater
                    .from(context)
                    .inflate(R.layout.model_empty_list_text, parent, false)
            )

            else -> MyViewHolder(
                LayoutInflater
                    .from(context)
                    .inflate(itemModel, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return if (itemCnt < 1) {
            emptyList = true
            0
        } else {
            itemCnt
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (emptyList) {
            DEFAULT_EMPTY
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!emptyList) {
            bindAction?.invoke(holder, position)
        } else {

        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            clickAction?.invoke(itemView, this)
        }
    }
}