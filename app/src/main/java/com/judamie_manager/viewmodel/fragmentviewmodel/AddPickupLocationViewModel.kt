package com.judamie_manager.viewmodel.fragmentviewmodel

import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.ui.fragment.AddCouponFragment
import com.judamie_manager.ui.fragment.AddPickupLocationFragment

class AddPickupLocationViewModel (val addPickupLocationFragment : AddPickupLocationFragment) : ViewModel(){

    // textFieldPickupName - text
    val textFieldPickupNameText = MutableLiveData("")
    // textFieldDolomyeong - text
    val textFieldDolomyeongText = MutableLiveData("")
    // textFieldDetailAddress - text
    val textFieldDetailAddressText = MutableLiveData("")
    // textFieldPhoneNumber - text
    val textFieldPhoneNumberText = MutableLiveData("")
    // textFieldAdditionalInfo - text
    val textFieldAdditionalInfoText = MutableLiveData("")


    // 데이터 초기화 메서드
    fun resetData() {
        textFieldPickupNameText.value = ""
        textFieldDolomyeongText.value = ""
        textFieldDetailAddressText.value = ""
        textFieldPhoneNumberText.value = ""
        textFieldAdditionalInfoText.value = ""
    }

    // buttonRegisterPickup - onClick
    fun buttonRegisterPickupOnClick(){
        addPickupLocationFragment.registerPickup()
    }

    // textFieldDolomyeong - onClick
    fun textFieldDolomyeongOnClick(){
        addPickupLocationFragment.setupDolomyeongApi()
    }

    companion object{
        // toolbarMain8 - onNavigationClickAddPickupLocation
        @JvmStatic
        @BindingAdapter("onNavigationClickAddPickupLocation")
        fun onNavigationClickAddPickupLocation(materialToolbar: MaterialToolbar, addPickupLocationFragment: AddPickupLocationFragment){
            materialToolbar.setNavigationOnClickListener {
                addPickupLocationFragment.movePrevFragment()
            }
        }
    }

}