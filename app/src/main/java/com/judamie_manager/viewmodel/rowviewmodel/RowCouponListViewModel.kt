package com.judamie_manager.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_manager.ui.fragment.CouponListFragment

class RowCouponListViewModel(val couponListFragment: CouponListFragment) : ViewModel(){
    // textViewCouponName - text
    val textViewCouponNameText = MutableLiveData("")
    // textViewCouponDate - text
    val textViewCouponDateText = MutableLiveData("")
}