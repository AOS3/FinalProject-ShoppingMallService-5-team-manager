package com.judamie_manager.viewmodel.componentviewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_manager.ui.component.ConfirmDialogFragment

class ConfirmDialogViewModel(val confirmDialogFragment: ConfirmDialogFragment) : ViewModel() {

    // pickupLocationName - text
    val pickupLocationNameText = MutableLiveData("")

    // pickupLocationDetail - text
    val pickupLocationDetailText = MutableLiveData("")

    // pickupLocationDetail - visibility
    val pickupLocationDetailTextVisibility = MutableLiveData(View.VISIBLE)

    // pickupLocationCall - onClick
    fun pickupLocationCallOnClick(){
        confirmDialogFragment.buttonCalling()
    }

    // pickupLocationClose - onClick
    fun pickupLocationCloseOnClick(){
        confirmDialogFragment.buttonClose()
    }
}