package com.judamie_manager.viewmodel.rowviewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_manager.ui.fragment.SettingPickupLocationFragment

class RowSettingPickupLocationViewModel(val settingPickupLocationFragment : SettingPickupLocationFragment) : ViewModel() {
    // textViewPickupName - text
    val textViewPickupNameText = MutableLiveData("")
    // checkBoxPickup - checkBox
    val checkBoxPickupChecked = MutableLiveData(false)

}