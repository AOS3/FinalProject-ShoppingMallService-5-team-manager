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
    fun checkBoxPickupAllOnClick() {
        // 전체 선택 상태에 맞게 처리
        val isChecked = checkBoxPickupAllChecked.value != true  // 현재 상태와 반대 값으로 설정
        checkBoxPickupAllChecked.value = isChecked  // 상태 갱신
        settingPickupLocationFragment.updateAllCheckBoxes(isChecked)  // 상태 업데이트 메서드 호출
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