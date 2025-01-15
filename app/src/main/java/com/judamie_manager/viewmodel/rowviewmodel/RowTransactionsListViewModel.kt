package com.judamie_manager.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_manager.ui.fragment.TransactionsListFragment

class RowTransactionsListViewModel (val transactionsListFragment: TransactionsListFragment) : ViewModel(){
    // textViewTransactionsList - text
    val textViewTransactionsListText = MutableLiveData("")
}