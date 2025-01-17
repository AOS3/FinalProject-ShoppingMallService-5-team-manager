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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentCompletedTransactionsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed_transactions_list, container, false)
        fragmentCompletedTransactionsListBinding.completedTransactionsListViewModel = CompletedTransactionsListViewModel(this@CompletedTransactionsListFragment)
        fragmentCompletedTransactionsListBinding.lifecycleOwner = this@CompletedTransactionsListFragment

        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 드롭다운 메뉴 연결 메서드 호출
        dropMenuAdapter()
        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()
        // search view 설정 메서드
        // initSearchView()


        // 검색 리사이클러뷰 구성 메서드 호출
        // settingRecyclerViewSearch()
        // SearchView 셋팅 메서드 호출
        // settingSearchView()
        // 메인 RecyclerView를 갱신하는 메서드
        // refreshMainRecyclerView()

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

//    // search view 설정 메서드
//    private fun initSearchView() {
//        // init SearchView
//        // isSubmitButtonEnabled = True : SearchView 안에 검색 버튼을 삭제
//        fragmentCompletedTransactionsListBinding.searchViewTransactionList.isSubmitButtonEnabled = true
//        // setOnQueryTextListener : 검색창에서 일어나는 event listener를 구현할 수 있음
//        fragmentCompletedTransactionsListBinding.searchViewTransactionList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            // onQueryTextSubmit : 검색을 완료하였을 경우 (키보드에 있는 '검색' 돋보기 버튼을 선택하였을 경우)
//            // return False : 검색 키보드를 내림
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // @TODO
//                return false
//            }
//
//            // onQueryTextChange : 검색어를 변경할 때마다 실행됨
//            override fun onQueryTextChange(newText: String?): Boolean {
//                // @TODO
//                return true
//            }
//        })
//    }

    // 검색 리사이클러뷰 구성 메서드
    fun settingRecyclerViewSearch() {
        fragmentCompletedTransactionsListBinding.apply {
            // RecyclerView에 어댑터를 설정한다.
            recyclerviewCompletedTransactionList.adapter = SearchRecyclerViewAdapter()
            // 한줄에 하나씩 배치한다
            recyclerviewCompletedTransactionList.layoutManager = LinearLayoutManager(serviceActivity)
            val deco = MaterialDividerItemDecoration(serviceActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerviewCompletedTransactionList.addItemDecoration(deco)
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


    fun settingSearchView() {
        fragmentCompletedTransactionsListBinding.apply {
            // SearchView에 대한 QueryTextListener 설정
            searchViewTransactionList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // 사용자가 입력한 검색어를 변수에 저장
                    searchKeyword = query.orEmpty()
                    // 검색 결과를 갱신
                    refreshSearchRecyclerView()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // 사용자가 입력 중인 검색어를 변수에 저장
                    searchKeyword = newText.orEmpty()
                    // 검색 결과를 실시간으로 갱신
                    refreshSearchRecyclerView()
                    return true
                }
            })

            // SearchView의 닫기 버튼 리스너 설정
            searchViewTransactionList.setOnCloseListener {
                // SearchView가 닫힐 때 처리
                isShownSearchView = false
                true
            }

            // SearchView의 열기 버튼 리스너 설정
            searchViewTransactionList.setOnSearchClickListener {
                // SearchView가 열릴 때 처리
                isShownSearchView = true
            }
        }
    }

    // 검색 결과를 갱신하는 메서드
    fun refreshSearchRecyclerView() {
        recyclerViewSearchList.clear()
        recyclerViewSearchList.addAll(tempList.filter { it.contains(searchKeyword, ignoreCase = true) })
        fragmentCompletedTransactionsListBinding.recyclerviewCompletedTransactionList.adapter?.notifyDataSetChanged()
    }

    // 메인 RecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView() {
        fragmentCompletedTransactionsListBinding.recyclerviewCompletedTransactionList.adapter?.notifyDataSetChanged()
    }
}