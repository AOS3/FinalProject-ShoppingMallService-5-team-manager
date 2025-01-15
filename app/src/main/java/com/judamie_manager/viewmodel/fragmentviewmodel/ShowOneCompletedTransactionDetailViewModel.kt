package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.ui.fragment.CompletedTransactionsListFragment
import com.judamie_manager.ui.fragment.ShowOneCompletedTransactionDetailFragment

class ShowOneCompletedTransactionDetailViewModel(val showOneCompletedTransactionDetailFragment: ShowOneCompletedTransactionDetailFragment) : ViewModel() {

    // textViewSeller - text
    val textViewSellerText = MutableLiveData("")
    // textViewCustomer - text
    val textViewCustomerText = MutableLiveData("")
    // textViewOrderDate - text
    val textViewOrderDateText = MutableLiveData("")
    // textViewTransactionDate - text
    val textViewTransactionDateText = MutableLiveData("")
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

    // toolbarMain4 - title
    val toolbarMain4Title = MutableLiveData("")

    companion object{
        // toolbarMain4 - onNavigationClickCompletedTransDetail
        @JvmStatic
        @BindingAdapter("onNavigationClickCompletedTransDetail")
        fun onNavigationClickCompletedTransDetail(materialToolbar: MaterialToolbar, showOneCompletedTransactionDetailFragment: ShowOneCompletedTransactionDetailFragment){
            materialToolbar.setNavigationOnClickListener {
                showOneCompletedTransactionDetailFragment.movePrevFragment()
            }
        }
    }
}