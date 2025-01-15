package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.ui.fragment.CouponListFragment

class CouponListViewModel (val couponListFragment: CouponListFragment) : ViewModel(){

    companion object{
        // toolbarBoardRead - onNavigationClickBoardRead
        @JvmStatic
        @BindingAdapter("onNavigationClickCouponList")
        fun onNavigationClickCouponList(materialToolbar: MaterialToolbar, couponListFragment: CouponListFragment){
            materialToolbar.setNavigationOnClickListener {
                couponListFragment.movePrevFragment()
            }
        }
    }
}