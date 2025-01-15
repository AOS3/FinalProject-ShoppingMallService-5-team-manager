package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.ui.fragment.ShowOneCompletedTransactionDetailFragment
import com.judamie_manager.ui.fragment.TransactionsListFragment

class TransactionsListViewModel(val transactionsListFragment: TransactionsListFragment) : ViewModel() {

    companion object{
        // toolbarMain5 - onNavigationClickTransactionsList
        @JvmStatic
        @BindingAdapter("onNavigationClickTransactionsList")
        fun onNavigationClickTransactionsList(materialToolbar: MaterialToolbar, transactionsListFragment: TransactionsListFragment){
            materialToolbar.setNavigationOnClickListener {
                transactionsListFragment.movePrevFragment()
            }
        }
    }
}