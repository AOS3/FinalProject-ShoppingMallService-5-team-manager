package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.ui.fragment.CouponListFragment
import com.judamie_manager.ui.fragment.ProcessingTransactionFragment

class ProcessingTransactionViewModel(val processingTransactionFragment: ProcessingTransactionFragment) : ViewModel(){

    // textViewSeller - text
    val textViewSellerText = MutableLiveData("")
    // textViewCustomer - text
    val textViewCustomerText = MutableLiveData("")
    // textViewOrderDate - text
    val textViewOrderDateText = MutableLiveData("")
    // textViewProduct - text
    val textViewProductText = MutableLiveData("")
    // textViewQuantity - text
    val textViewQuantityText = MutableLiveData("")
    // textViewPrice - text
    val textViewPriceText = MutableLiveData("")
    // textViewTotalPrice - text
    val textViewTotalPriceText = MutableLiveData("")
    // textViewDelivery - text
    val textViewDeliveryText = MutableLiveData("")
    // textViewPickup - text
    val textViewPickupText = MutableLiveData("")

    // toolbarMain6 - title
    val toolbarMain6Title = MutableLiveData("")

    // buttonDeposit - onClick
    fun buttonDepositOnClick(){
        processingTransactionFragment.buttonDeposit()
    }

    companion object{
        // toolbar6 - onNavigationClickProcessingTransaction
        @JvmStatic
        @BindingAdapter("onNavigationClickProcessingTransaction")
        fun onNavigationClickProcessingTransaction(materialToolbar: MaterialToolbar, processingTransactionFragment: ProcessingTransactionFragment){
            materialToolbar.setNavigationOnClickListener {
                processingTransactionFragment.movePrevFragment()
            }
        }
    }
}