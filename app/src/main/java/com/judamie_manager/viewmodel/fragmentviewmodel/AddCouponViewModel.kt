package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.ui.fragment.AddCouponFragment
import com.judamie_manager.ui.fragment.CouponListFragment

class AddCouponViewModel (val addCouponFragment: AddCouponFragment) : ViewModel() {

    // textFieldAddCouponName - text
    val textFieldAddCouponNameText = MutableLiveData("")
    // textFieldAddCouponSale - text
    val textFieldAddCouponSaleText = MutableLiveData("")
    // textFieldAddCouponDate - text
    val textFieldAddCouponDateText = MutableLiveData("")

    // textFieldAddCouponDate - onClick
    fun textFieldAddCouponDateOnClick(){
        addCouponFragment.setupDatePicker()
    }

    companion object{
        // toolbarMain2 - onNavigationClickAddCoupon
        @JvmStatic
        @BindingAdapter("onNavigationClickAddCoupon")
        fun onNavigationClickAddCoupon(materialToolbar: MaterialToolbar, addCouponFragment: AddCouponFragment){
            materialToolbar.setNavigationOnClickListener {
                addCouponFragment.movePrevFragment()
            }
        }
    }
}