package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.ui.fragment.AddCouponFragment
import com.judamie_manager.ui.fragment.CompletedTransactionsListFragment

class CompletedTransactionsListViewModel(val completedTransactionsListFragment: CompletedTransactionsListFragment) : ViewModel() {

    companion object{
        // toolbarMain3 - onNavigationClickCompletedTransList
        @JvmStatic
        @BindingAdapter("onNavigationClickCompletedTransList")
        fun onNavigationClickCompletedTransList(materialToolbar: MaterialToolbar, completedTransactionsListFragment: CompletedTransactionsListFragment){
            materialToolbar.setNavigationOnClickListener {
                completedTransactionsListFragment.movePrevFragment()
            }
        }
    }
}