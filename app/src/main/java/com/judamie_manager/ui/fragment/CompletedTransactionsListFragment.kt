package com.judamie_manager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
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
import com.judamie_manager.firebase.model.OrderDataModel
import com.judamie_manager.firebase.repository.OrderRepository
import com.judamie_manager.viewmodel.fragmentviewmodel.AddCouponViewModel
import com.judamie_manager.viewmodel.fragmentviewmodel.CompletedTransactionsListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowCompletedTransactionsListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowCouponListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class CompletedTransactionsListFragment : Fragment() {

    lateinit var fragmentCompletedTransactionsListBinding: FragmentCompletedTransactionsListBinding
    lateinit var serviceActivity: ServiceActivity

    // 프레그레스바를 위한 변수
    lateinit var progressBar: ProgressBar

//    // ReyclerView 구성을 위한 임시 데이터
//    val tempList = Array(100) {
//        "판매자 ${it + 1} <-> 구매자 ${it + 1}"
//    }

    // RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewList = mutableListOf<OrderDataModel>()
    // 판매자 이름 리스트
    var sellerNameList =  mutableListOf<String>()
    // 구매자 이름 리스트
    var userNameList = mutableListOf<String>()
    // 픽업지 이름 리스트
    var pickupNameList = mutableListOf<String>()
    // 상품 이름 리스트
    var productNameList = mutableListOf<String>()
    // 상품 카테고리 리스트
    var productCategoryList = mutableListOf<String>()
    var isProgressVisible = false


    // 검색 RecyclerView 임시 리스트
    var recyclerViewSearchList = mutableListOf<OrderDataModel>()
    // var recyclerViewSearchList = mutableListOf<String>()
    // private lateinit var recyclerViewSearchList: List<TransactionItem>
    // 드롭다운에서 선택된 카테고리 값
    var selectedCategory = ""
    // 현재 SearchView가 보여지고 있는지.
    var isShownSearchView = false
    // 검색어를 담을 변수
    var searchKeyword = ""

    // 드롭다운 메뉴 초기화 (온클릭 눌러서 갔다가 돌아와도 실행되도록,,,)
    override fun onResume() {
        super.onResume()
        dropMenuAdapter()

        // 처음에만 프로그레스바를 띄우고 이후에는 띄우지 않음
        if (!isProgressVisible) {
            fragmentCompletedTransactionsListBinding.progressBar.visibility = View.VISIBLE
            isProgressVisible = true
            refreshRecyclerView()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentCompletedTransactionsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed_transactions_list, container, false)
        fragmentCompletedTransactionsListBinding.completedTransactionsListViewModel = CompletedTransactionsListViewModel(this@CompletedTransactionsListFragment)
        fragmentCompletedTransactionsListBinding.lifecycleOwner = this@CompletedTransactionsListFragment

        serviceActivity = activity as ServiceActivity

        // 프레그레스바 초기화
        progressBar = fragmentCompletedTransactionsListBinding.progressBar


        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()
        // 리사이클러뷰 갱신 메서드 호출
        // refreshRecyclerView()
        // 검색 리사이클러뷰 구성 메서드 호출
        refreshSearchRecyclerView()
        // SearchView 셋팅 메서드 호출
        settingSearchView()
        // 메인 RecyclerView를 갱신하는 메서드
        refreshMainRecyclerView()
        // 검색 리사이클러뷰 구성 메서드
        settingRecyclerViewSearch()
        // 카테고리 선택 확인 및 처리
        handleCategorySelection()

        return fragmentCompletedTransactionsListBinding.root

    }


    // 데이터를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshRecyclerView() {

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                OrderRepository.getOrderDataWithState(4)
            }
            recyclerViewList = work1.await()

            // recyclerViewList가 비어 있으면 갱신하지 않음
            if (recyclerViewList.isNotEmpty()) {
                val sellerIds = recyclerViewList.map { it.sellerDocumentID }
                val userIds = recyclerViewList.map { it.userDocumentID }
                val pickupIds = recyclerViewList.map { it.pickupLocDocumentID }
                val productIds = recyclerViewList.map { it.productDocumentID }

                val sellerNamesDeferred = async(Dispatchers.IO) {
                    OrderRepository.getSellerNamesByIds(sellerIds)
                }
                val userNamesDeferred = async(Dispatchers.IO) {
                    OrderRepository.getUserNamesByIds(userIds)
                }
                val pickupNamesDeferred = async(Dispatchers.IO) {
                    OrderRepository.getPickupNamesByIds(pickupIds)
                }
                val productNamesDeferred = async(Dispatchers.IO) {
                    OrderRepository.getProductNamesByIds(productIds)
                }
                val productCategoriesDeferred = async(Dispatchers.IO) {
                    OrderRepository.getProductCategoryByIds(productIds)
                }

                // OrderData 가져오기
                recyclerViewList = work1.await()


                // 모든 데이터를 기다린 후 처리
                sellerNameList = sellerNamesDeferred.await().toMutableList()
                userNameList = userNamesDeferred.await().toMutableList()
                pickupNameList = pickupNamesDeferred.await().toMutableList()
                productNameList = productNamesDeferred.await().toMutableList()
                productCategoryList = productCategoriesDeferred.await().toMutableList()

                fragmentCompletedTransactionsListBinding.progressBar.visibility = View.GONE

                fragmentCompletedTransactionsListBinding.recyclerviewCompletedTransactionList.adapter?.notifyDataSetChanged()


            }
        }
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

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val recyclerViewData = recyclerViewList[position]
            holder.rowCompletedTransactionsListBinding.rowCompletedTransactionsListViewModel?.textViewCompletedTransListText?.value = "판매자 : ${sellerNameList[position]} <-> 구매자 : ${userNameList[position]}"
            holder.rowCompletedTransactionsListBinding.apply{
                root.setOnClickListener {
                    val dataBundle = Bundle().apply {
                        putString("orderDocumentID", recyclerViewData.orderDocumentID)
                        putString("sellerName", sellerNameList[position])
                        putString("userName", userNameList[position])
                        putString("pickupName", pickupNameList[position])
                        putString("productName", productNameList[position])
                        putString("orderTime", recyclerViewData.orderTime.toString())
                        putString("productPrice", recyclerViewData.productPrice.toString())
                        putString("productDiscountRate",
                            recyclerViewData.productDiscountRate.toString()
                        )
                        putString("orderCount", recyclerViewData.orderCount.toString())
                        putString("orderPriceAmount", recyclerViewData.orderPriceAmount.toString())
                        putString("orderState", recyclerViewData.orderState.toString())

                    }

                    serviceActivity.replaceFragment(ServiceFragmentName.SHOW_ONE_COMPLETED_TRANSACTION_DETAIL_FRAGMENT, true, true, dataBundle)
                }
            }
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


    // 메인 RecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView() {
        fragmentCompletedTransactionsListBinding.recyclerviewCompletedTransactionList.adapter?.notifyDataSetChanged()
    }


    // 검색 리사이클러뷰 갱신 메서드
    fun refreshSearchRecyclerView() {
        recyclerViewSearchList.clear()

        // 카테고리가 선택되지 않았으면 오류 메시지를 표시
        if (selectedCategory.isEmpty()) {
            fragmentCompletedTransactionsListBinding.textViewNoCategorySelected.visibility = View.VISIBLE
            return
        } else {
            fragmentCompletedTransactionsListBinding.textViewNoCategorySelected.visibility = View.GONE
        }

        // 검색어가 없으면 전체 목록을 보여주고, 검색어가 있으면 필터링
        if (searchKeyword.isEmpty()) {
            recyclerViewSearchList.clear()
        } else {
            // 검색어가 있을 경우 필터링
            val filteredList = recyclerViewList.filterIndexed { index, item ->
                when (selectedCategory) {
                    "판매자" -> sellerNameList[index].contains(searchKeyword, ignoreCase = true)
                    "구매자" -> userNameList[index].contains(searchKeyword, ignoreCase = true)
                    "제품명" -> productNameList[index].contains(searchKeyword, ignoreCase = true)
                    "카테고리" -> productCategoryList[index].contains(searchKeyword, ignoreCase = true)
                    else -> false
                }
            }

            recyclerViewSearchList.clear()
            recyclerViewSearchList.addAll(filteredList) // 필터링된 데이터를 추가
        }

        // 검색 결과가 없으면 "결과가 없습니다" 메시지 표시
        if (recyclerViewSearchList.isEmpty()) {
            fragmentCompletedTransactionsListBinding.textViewNoResults.visibility = View.VISIBLE
        } else {
            fragmentCompletedTransactionsListBinding.textViewNoResults.visibility = View.GONE
        }

        // RecyclerView 갱신
        fragmentCompletedTransactionsListBinding.recyclerviewCompletedTransactionListSearch.adapter?.notifyDataSetChanged()
    }


    // 드롭다운 메뉴 초기화
    private fun dropMenuAdapter() {
        val categoryArray = resources.getStringArray(R.array.select_category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryArray)
        fragmentCompletedTransactionsListBinding.autoCompleteTextView.setAdapter(arrayAdapter)

        fragmentCompletedTransactionsListBinding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = categoryArray[position]
        }
    }

    // 검색 리사이클러뷰 어댑터
    inner class SearchRecyclerViewAdapter : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {
        inner class SearchViewHolder(val binding: RowCompletedTransactionsListBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val binding = DataBindingUtil.inflate<RowCompletedTransactionsListBinding>(
                layoutInflater, R.layout.row_completed_transactions_list, parent, false
            )
            binding.rowCompletedTransactionsListViewModel = RowCompletedTransactionsListViewModel(this@CompletedTransactionsListFragment)
            binding.lifecycleOwner = this@CompletedTransactionsListFragment
            return SearchViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return recyclerViewSearchList.size
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            val item = recyclerViewSearchList[position]
//            // 필터링된 리스트에서 가져온 item을 기준으로 데이터 설정
//            val seller = sellerNameList[recyclerViewList.indexOf(item)]  // 필터링된 리스트에서 실제 데이터의 인덱스를 찾음
//            val buyer = userNameList[recyclerViewList.indexOf(item)]
//            productCategoryList[recyclerViewList.indexOf(item)]


            // 필터링된 리스트에서 원본 리스트의 정확한 인덱스를 가져옴
            val originalIndex = recyclerViewList.indexOf(item)
            if (originalIndex == -1) return  // 원본 리스트에 해당 항목이 없으면 리턴

            val seller = sellerNameList[originalIndex]
            val buyer = userNameList[originalIndex]
            val pickup = pickupNameList[originalIndex]
            val product = productNameList[originalIndex]

            // 판매자와 구매자를 나란히 보여주는 방식
            holder.binding.rowCompletedTransactionsListViewModel?.textViewCompletedTransListText?.value = "${seller} <-> ${buyer}"

            holder.binding.apply{
                root.setOnClickListener {
                    val dataBundle = Bundle().apply {
                        putString("orderDocumentID", item.orderDocumentID)
                        putString("sellerName", seller)
                        putString("userName", buyer)
                        putString("pickupName", pickup)
                        putString("productName", product)
                        putString("orderTime", item.orderTime.toString())
                        putString("productPrice", item.productPrice.toString())
                        putString("productDiscountRate",
                            item.productDiscountRate.toString()
                        )
                        putString("orderCount", item.orderCount.toString())
                        putString("orderPriceAmount", item.orderPriceAmount.toString())
                        putString("orderState", item.orderState.toString())

                    }

                    serviceActivity.replaceFragment(ServiceFragmentName.SHOW_ONE_COMPLETED_TRANSACTION_DETAIL_FRAGMENT, true, true, dataBundle)
                }
            }
        }
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

    // SearchView 세팅 메서드
    fun settingSearchView() {
        fragmentCompletedTransactionsListBinding.apply {
            // SearchView의 입력 창에 대한 엔터키 이벤트
            searchViewTransactionList.editText.setOnEditorActionListener { v, actionId, event ->
                // 사용자가 입력한 검색어를 가져와 변수에 담아둔다.
                searchKeyword = searchViewTransactionList.editText.text.toString()

                // 드롭다운 메뉴에서 선택된 카테고리를 확인
                val selectedCategory = autoCompleteTextView.text.toString()

                // 카테고리가 선택되지 않으면 경고 메시지를 띄운다
                if (selectedCategory.isEmpty()) {
                    fragmentCompletedTransactionsListBinding.textViewNoResults.visibility = View.VISIBLE
                    return@setOnEditorActionListener true
                }

                // 검색 결과를 가져와 RecyclerView를 갱신하는 메서드를 호출한다.
                refreshSearchRecyclerView()
                true
            }

            // SearchView의 상태 변화에 대한 이벤트
            searchViewTransactionList.addTransitionListener { searchView, previousState, newState ->
                when (newState) {
                    // SearchView가 보이기 전
                    com.google.android.material.search.SearchView.TransitionState.SHOWING -> {
                        isShownSearchView = true
                        recyclerViewSearchList.clear()
                    }
                    // SearchView가 보인 후
                    com.google.android.material.search.SearchView.TransitionState.SHOWN -> {
                        // 상태 변경 후 추가할 동작이 있다면 여기에 구현
                    }
                    // SearchView가 사라지기 전
                    com.google.android.material.search.SearchView.TransitionState.HIDING -> {
                        // 상태 변경 전 추가할 동작이 있다면 여기에 구현
                    }
                    // SearchView가 사라진 후
                    com.google.android.material.search.SearchView.TransitionState.HIDDEN -> {
                        isShownSearchView = false
                        recyclerViewSearchList.clear()
                        recyclerviewCompletedTransactionListSearch.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // 카테고리 선택 확인 및 처리
    fun handleCategorySelection() {
        // 카테고리가 선택되지 않은 상태에서 검색을 시도하면 경고 메시지를 표시
        if (selectedCategory.isEmpty()) {
            fragmentCompletedTransactionsListBinding.textViewNoCategorySelected.visibility = View.VISIBLE
        } else {
            fragmentCompletedTransactionsListBinding.textViewNoCategorySelected.visibility = View.GONE
        }
    }

}