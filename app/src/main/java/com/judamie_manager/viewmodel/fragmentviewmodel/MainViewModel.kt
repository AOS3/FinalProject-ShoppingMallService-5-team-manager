package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.lifecycle.ViewModel
import com.judamie_manager.ui.fragment.MainFragment

class MainViewModel (val mainFragment: MainFragment) : ViewModel() {
    // buttonMainCoupon - onClick
    fun buttonMainCouponOnClick(){
        mainFragment.moveToMainCoupon()
    }

    // buttonMainTranList - onClick
    fun buttonMainTranListOnClick(){
        mainFragment.moveToMainTranList()
    }

    // buttonMainTranCompleted - onClick
    fun buttonMainTranDetailOnClick(){
        mainFragment.moveToMainTranCompleted()
    }

    // buttonMainPickup - onClick
    fun buttonMainPickupOnClick(){
        mainFragment.moveToMainCouponPickup()
    }
}