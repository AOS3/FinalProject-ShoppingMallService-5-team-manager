package com.judamie_manager.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_manager.ui.fragment.CompletedTransactionsListFragment

class RowCompletedTransactionsListViewModel (val completedTransactionsListFragment: CompletedTransactionsListFragment) : ViewModel() {
    // textViewCompletedTransList - text
    val textViewCompletedTransListText = MutableLiveData("")
}