package com.judamie_manager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentTransactionsListBinding
import com.judamie_manager.databinding.RowCompletedTransactionsListBinding
import com.judamie_manager.databinding.RowTransactionsListBinding
import com.judamie_manager.viewmodel.fragmentviewmodel.TransactionsListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowCompletedTransactionsListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowTransactionsListViewModel

class TransactionsListFragment : Fragment() {
    lateinit var fragmentTransactionsListBinding: FragmentTransactionsListBinding
    lateinit var serviceActivity: ServiceActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList = Array(100) {
        "판매자 ${it + 1} <-> 구매자 ${it + 1}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentTransactionsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactions_list, container, false)
        fragmentTransactionsListBinding.transactionsListViewModel = TransactionsListViewModel(this@TransactionsListFragment)
        fragmentTransactionsListBinding.lifecycleOwner = this@TransactionsListFragment

        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()

        return fragmentTransactionsListBinding.root

    }

    // 리사이클러뷰 구성 메서드
    fun settingRecyclerView() {
        fragmentTransactionsListBinding.apply {
            // RecyclerView에 어댑터를 설정한다.
            recyclerviewTransactionList.adapter = RecyclerViewAdapter()
            // 한줄에 하나씩 배치한다
            recyclerviewTransactionList.layoutManager = LinearLayoutManager(serviceActivity)
            val deco = MaterialDividerItemDecoration(serviceActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerviewTransactionList.addItemDecoration(deco)
        }
    }

    // 메인 RecyclerView의 어뎁터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>(){
        inner class MainViewHolder(val rowTransactionsListBinding: RowTransactionsListBinding) : RecyclerView.ViewHolder(rowTransactionsListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowTransactionsListBinding = DataBindingUtil.inflate<RowTransactionsListBinding>(layoutInflater, R.layout.row_transactions_list, parent, false)
            rowTransactionsListBinding.rowTransactionsListViewModel = RowTransactionsListViewModel(this@TransactionsListFragment)
            rowTransactionsListBinding.lifecycleOwner = this@TransactionsListFragment

            val mainViewHolder = MainViewHolder(rowTransactionsListBinding)

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowTransactionsListBinding.root.setOnClickListener {

                serviceActivity.replaceFragment(ServiceFragmentName.PROCESSING_TRANSACTION_FRAGMENT, true, true, null)
            }

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return tempList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowTransactionsListBinding.rowTransactionsListViewModel?.textViewTransactionsListText?.value = tempList[position]
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        serviceActivity.removeFragment(ServiceFragmentName.TRANSACTIONS_LIST_FRAGMENT)
    }

    // 툴바를 구성하는 메서드
    private fun settingToolbar(){
        fragmentTransactionsListBinding.apply {
            toolbarMain5.title = "거래 처리"
        }
    }

}