package com.tech.ssylix.alc4phase1challenge2.logic.natives.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProviders
import com.tech.ssylix.alc4phase1challenge2.R
import com.tech.ssylix.alc4phase1challenge2.data.models.Deal
import com.tech.ssylix.alc4phase1challenge2.logic.viewmodel.MainViewModel
import com.tech.ssylix.alc4phase1challenge2.presenter.animateClicks
import com.tech.ssylix.alc4phase1challenge2.presenter.toast
import kotlinx.android.synthetic.main.fragment_new_destination.*
import kotlinx.android.synthetic.main.fragment_new_destination.view.*


class NewDestination : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    val mViewModel : MainViewModel by lazy {
        ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_destination, container, false)
    }

    var mImageSelected: Boolean = false
    var mImageUri : Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.select_image.setOnClickListener {
            it.animateClicks(50) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                startActivityForResult(Intent.createChooser(intent, "Get Images"), REQ_IMG)
            }
        }
        view.save_deal.setOnClickListener {
            it.animateClicks {
                if(view.destination_textInputLayout.isNotEmpty() && view.cost_textInputLayout.isNotEmpty() &&
                    view.accomodation_textInputLayout.isNotEmpty() && mImageSelected){
                    val dest = view.destination_text.text.toString()
                    val cost = view.cost_text.text.toString()
                    val accoms = view.accomodation_text.text.toString()
                    onSaveButtonPressed(Deal(dest, cost, accoms, null), mImageUri!!)
                }else{
                    context?.toast("You need to fill fields")
                }
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onSaveButtonPressed(deal: Deal, uri: Uri) {
        mViewModel.mFirebaseUtils.uploadNewDeal(deal, uri, view?.imageUploadProgress!!){
            view?.destination_text?.setText("")
            view?.cost_text?.setText("")
            view?.accomodation_text?.setText("")
            view?.deal_image?.setImageResource(R.drawable.googleg_standard_color_18)
            view?.save_deal?.isEnabled = false
            mImageSelected = false
            mImageUri = null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQ_IMG -> {
                if(resultCode == RESULT_OK){
                    view?.save_deal?.isEnabled = true
                    mImageSelected = true
                    val imageUri = data?.data
                    view?.deal_image?.setImageURI(imageUri)
                    mImageUri = imageUri
                }
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onSaveNewDeal(deal: Deal, uri: Uri){}
    }

    companion object {
        const val REQ_IMG = 1011
    }
}
