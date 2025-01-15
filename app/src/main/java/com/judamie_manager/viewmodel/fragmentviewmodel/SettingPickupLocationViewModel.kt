package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.ui.fragment.ProcessingTransactionFragment
import com.judamie_manager.ui.fragment.SettingPickupLocationFragment

class SettingPickupLocationViewModel(val settingPickupLocationFragment : SettingPickupLocationFragment) : ViewModel() {

    // buttonSelectionDelete - onClick
    fun buttonSelectionDeleteOnClick(){
        settingPickupLocationFragment.selectionDelete()
    }

    // fabAddPickup - onClick
    fun fabAddPickupOnClick(){
        settingPickupLocationFragment.settingFabAddPickup()
    }

    // checkBoxPickupAll - checkBox
    val checkBoxPickupAllChecked = MutableLiveData(false)

    // checkBoxPickupAll - onClick
    fun checkBoxPickupAllOnClick(){
        if (checkBoxPickupAllChecked.value == true){
            val isChecked = true
            settingPickupLocationFragment.updateAllCheckBoxes(isChecked)
        }
        else {
            val isChecked = false
            settingPickupLocationFragment.updateAllCheckBoxes(isChecked)
        }
    }

    companion object{
        // toolbar7 - onNavigationClickSettingPickupLocation
        @JvmStatic
        @BindingAdapter("onNavigationClickSettingPickupLocation")
        fun onNavigationClickSettingPickupLocation(materialToolbar: MaterialToolbar, settingPickupLocationFragment: SettingPickupLocationFragment){
            materialToolbar.setNavigationOnClickListener {
                settingPickupLocationFragment.movePrevFragment()
            }
        }
    }
}