package com.judamie_manager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.compose.animation.core.TransitionState
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentAddCouponBinding
import com.judamie_manager.databinding.FragmentCompletedTransactionsListBinding
import com.judamie_manager.databinding.RowCompletedTransactionsListBinding
import com.judamie_manager.databinding.RowCouponListBinding
import com.judamie_manager.viewmodel.fragmentviewmodel.AddCouponViewModel
import com.judamie_manager.viewmodel.fragmentviewmodel.CompletedTransactionsListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowCompletedTransactionsListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowCouponListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CompletedTransactionsListFragment : Fragment() {

    lateinit var fragmentCompletedTransactionsListBinding: FragmentCompletedTransactionsListBinding
    lateinit var serviceActivity: ServiceActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList = Array(100) {
        "판매자 ${it + 1} <-> 구매자 ${it + 1}"
    }

    // 검색 RecyclerView 임시 리스트
    var recyclerViewSearchList = mutableListOf<String>()

    // 현재 SearchView가 보여지고 있는지.
    var isShownSearchView = false
    // 검색어를 담을 변수
    var searchKeyword = ""

    // 드롭다운 메뉴 초기화 (온클릭 눌러서 갔다가 돌아와도 실행되도록,,,)
    override fun onResume() {
        super.onResume()
        dropMenuAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentCompletedTransactionsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed_transactions_list, container, false)
        fragmentCompletedTransactionsListBinding.completedTransactionsListViewModel = CompletedTransactionsListViewModel(this@CompletedTransactionsListFragment)
        fragmentCompletedTransactionsListBinding.lifecycleOwner = this@CompletedTransactionsListFragment

        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()
        // search view 설정 메서드
        // initSearchView()


        // 검색 리사이클러뷰 구성 메서드 호출
        settingRecyclerViewSearch()
        // SearchView 셋팅 메서드 호출
        settingSearchView()
        // 메인 RecyclerView를 갱신하는 메서드
        refreshMainRecyclerView()

        // 만약 검색화면이 보여지고 있는 상태라면..
        if(isShownSearchView == true){
            refreshSearchRecyclerView()
        }

        return fragmentCompletedTransactionsListBinding.root

    }

    // 메인 리사이클러뷰 구성 메서드
    fun settingRecyclerView() {
        fragmentCompletedTransactionsListBinding.apply {
            // RecyclerView에 어댑터를 설정한다.
            recyclerviewCompletedTransactionList.adapter = RecyclerViewAdapter()
            // 한줄에 하나씩 배치한다
            recyclerviewCompletedTransactionList.layoutManager = LinearLayoutManager(serviceActivity)
            val deco = MaterialDividerItemDecoration(serviceActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerviewCompletedTransactionList.addItemDecoration(deco)
        }
    }

    // 메인 RecyclerView의 어뎁터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>(){
        inner class MainViewHolder(val rowCompletedTransactionsListBinding: RowCompletedTransactionsListBinding) : RecyclerView.ViewHolder(rowCompletedTransactionsListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowCompletedTransactionsListBinding = DataBindingUtil.inflate<RowCompletedTransactionsListBinding>(layoutInflater, R.layout.row_completed_transactions_list, parent, false)
            rowCompletedTransactionsListBinding.rowCompletedTransactionsListViewModel = RowCompletedTransactionsListViewModel(this@CompletedTransactionsListFragment)
            rowCompletedTransactionsListBinding.lifecycleOwner = this@CompletedTransactionsListFragment

            val mainViewHolder = MainViewHolder(rowCompletedTransactionsListBinding)

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowCompletedTransactionsListBinding.root.setOnClickListener {

                serviceActivity.replaceFragment(ServiceFragmentName.SHOW_ONE_COMPLETED_TRANSACTION_DETAIL_FRAGMENT, true, true, null)
            }

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return tempList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowCompletedTransactionsListBinding.rowCompletedTransactionsListViewModel?.textViewCompletedTransListText?.value = tempList[position]
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        serviceActivity.removeFragment(ServiceFragmentName.COMPLETED_TRANSACTIONS_LIST_FRAGMENT)
    }

    // 툴바를 구성하는 메서드
    private fun settingToolbar(){
        fragmentCompletedTransactionsListBinding.apply {
            toolbarMain3.title = "거래 내역"
        }
    }

    // 드롭다운메뉴 ArrayAdapter 연결 메서드
    private fun dropMenuAdapter(){
        val categoryArray = resources.getStringArray(R.array.select_category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryArray)
        fragmentCompletedTransactionsListBinding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    // 검색 리사이클러뷰 구성 메서드
    fun settingRecyclerViewSearch() {
        fragmentCompletedTransactionsListBinding.apply {
            // RecyclerView에 어댑터를 설정한다.
            recyclerviewCompletedTransactionListSearch.adapter = SearchRecyclerViewAdapter()
            // 한줄에 하나씩 배치한다
            recyclerviewCompletedTransactionListSearch.layoutManager = LinearLayoutManager(serviceActivity)
            val deco = MaterialDividerItemDecoration(serviceActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerviewCompletedTransactionListSearch.addItemDecoration(deco)
        }
    }

    // 검색 RecyclerView의 어뎁터
    inner class SearchRecyclerViewAdapter : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>(){
        inner class SearchViewHolder(val rowCompletedTransactionsListBinding: RowCompletedTransactionsListBinding) : RecyclerView.ViewHolder(rowCompletedTransactionsListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val rowCompletedTransactionsListBinding = DataBindingUtil.inflate<RowCompletedTransactionsListBinding>(layoutInflater, R.layout.row_completed_transactions_list, parent, false)
            rowCompletedTransactionsListBinding.rowCompletedTransactionsListViewModel = RowCompletedTransactionsListViewModel(this@CompletedTransactionsListFragment)
            rowCompletedTransactionsListBinding.lifecycleOwner = this@CompletedTransactionsListFragment

            val searchViewHolder = SearchViewHolder(rowCompletedTransactionsListBinding)

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowCompletedTransactionsListBinding.root.setOnClickListener {
                serviceActivity.replaceFragment(ServiceFragmentName.SHOW_ONE_COMPLETED_TRANSACTION_DETAIL_FRAGMENT, true, true, null)
            }

            return searchViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewSearchList.size
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            holder.rowCompletedTransactionsListBinding.rowCompletedTransactionsListViewModel?.textViewCompletedTransListText?.value = recyclerViewSearchList[position]
        }
    }

    // SearchView 세팅 메서드
    fun settingSearchView(){
        fragmentCompletedTransactionsListBinding.apply {
            // SerachView의 입력 창에 대한 엔터키 이벤트
            searchViewTransactionList.editText.setOnEditorActionListener { v, actionId, event ->
                // 사용자가 입력한 검색어를 가져와 변수에 담아둔다.
                searchKeyword = searchViewTransactionList.editText.text.toString()
                // 검색 결과를 가져와 RecyclerView를 갱신하는 메서드를 호출한다.
                refreshSearchRecyclerView()
                true
            }

            // SearchView 노출에 대한 이벤트
            searchViewTransactionList.addTransitionListener { searchView, previousState, newState ->

                when(newState){
                    // 보이기 전
                    com.google.android.material.search.SearchView.TransitionState.SHOWING -> {
                        isShownSearchView = true
                        recyclerViewSearchList.clear()
                    }
                    // 보이고난 후
                    com.google.android.material.search.SearchView.TransitionState.SHOWN -> {

                    }
                    // 사라지기 전
                    com.google.android.material.search.SearchView.TransitionState.HIDING -> {

                    }
                    // 사라지고 난 후
                    com.google.android.material.search.SearchView.TransitionState.HIDDEN -> {
                        isShownSearchView = false
                        recyclerViewSearchList.clear()
                        recyclerviewCompletedTransactionListSearch.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // 검색 결과를 갱신하는 메서드
    fun refreshSearchRecyclerView() {
        recyclerViewSearchList.clear()
        recyclerViewSearchList.addAll(tempList.filter { it.contains(searchKeyword, ignoreCase = true) })
        // 결과가 없으면 "결과가 없습니다" 메시지를 보이고, 결과가 있으면 메시지를 숨긴다
        if (recyclerViewSearchList.isEmpty()) {
            fragmentCompletedTransactionsListBinding.textViewNoResults.visibility = View.VISIBLE
        } else {
            fragmentCompletedTransactionsListBinding.textViewNoResults.visibility = View.GONE
        }
        // 검색 결과에 맞는 RecyclerView의 어댑터를 갱신
        fragmentCompletedTransactionsListBinding.recyclerviewCompletedTransactionListSearch.adapter?.notifyDataSetChanged()
    }


    // 메인 RecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView() {
        fragmentCompletedTransactionsListBinding.recyclerviewCompletedTransactionList.adapter?.notifyDataSetChanged()
    }
}