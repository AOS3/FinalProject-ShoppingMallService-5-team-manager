package com.judamie_manager.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentTransactionsListBinding
import com.judamie_manager.databinding.RowTransactionsListBinding
import com.judamie_manager.firebase.model.OrderDataModel
import com.judamie_manager.firebase.repository.OrderRepository
import com.judamie_manager.viewmodel.fragmentviewmodel.TransactionsListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowTransactionsListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionsListFragment : Fragment() {
    lateinit var fragmentTransactionsListBinding: FragmentTransactionsListBinding
    lateinit var serviceActivity: ServiceActivity


    // RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewList = mutableListOf<OrderDataModel>()

    // 판매자 이름 리스트
    var sellerNameList =  mutableListOf<String>()
    // 구매자 이름 리스트
    var userNameList = mutableListOf<String>()
    // 픽업지 이름 리스트
    // var pickupNameList = mutableListOf<String>()
    // 상품 이름 리스트
    var productNameList = mutableListOf<String>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentTransactionsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactions_list, container, false)
        fragmentTransactionsListBinding.transactionsListViewModel = TransactionsListViewModel(this@TransactionsListFragment)
        fragmentTransactionsListBinding.lifecycleOwner = this@TransactionsListFragment

        serviceActivity = activity as ServiceActivity

        // RecyclerView 구성을 위한 리스트를 초기화한다.
        recyclerViewList.clear()

        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()
        // 리사이클러뷰 걍신 메서드 호출
        refreshRecyclerView()

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


    // 데이터를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshRecyclerView(){
        fragmentTransactionsListBinding.progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                OrderRepository.getOrderDataWithoutState(4)
            }
            recyclerViewList = work1.await()

            // recyclerViewList가 비어 있으면 갱신하지 않음
            if (recyclerViewList.isNotEmpty()) {
                //val sellerIds = recyclerViewList.map { it.sellerDocumentID }
                val userIds = recyclerViewList.map { it.userDocumentID }
                // val pickupIds = recyclerViewList.map { it.pickupLocDocumentID }
                val productIds = recyclerViewList.map { it.productDocumentID }

//                val sellerNamesDeferred = async(Dispatchers.IO) {
//                    OrderRepository.getSellerNamesByIds(sellerIds)
//                }
                val userNamesDeferred = async(Dispatchers.IO) {
                    OrderRepository.getUserNamesByIds(userIds)
                }
//                val pickupNamesDeferred = async(Dispatchers.IO) {
//                    OrderRepository.getPickupNamesByIds(pickupIds)
//                }
                val productNamesDeferred = async(Dispatchers.IO) {
                    OrderRepository.getProductNamesByIds(productIds)
                }

                // OrderData 가져오기
                recyclerViewList = work1.await()

                // 모든 데이터를 기다린 후 처리
                //sellerNameList = sellerNamesDeferred.await().toMutableList()
                userNameList = userNamesDeferred.await().toMutableList()
                // pickupNameList = pickupNamesDeferred.await().toMutableList()
                productNameList = productNamesDeferred.await().toMutableList()

                fragmentTransactionsListBinding.progressBar.visibility = View.GONE


                fragmentTransactionsListBinding.recyclerviewTransactionList.adapter?.notifyDataSetChanged()


            }else{
                // 데이터가 비어 있을 경우 UI를 업데이트
                fragmentTransactionsListBinding.progressBar.visibility = View.GONE

                //sellerNameList.clear()
                userNameList.clear()
                productNameList.clear()

                fragmentTransactionsListBinding.recyclerviewTransactionList.adapter?.notifyDataSetChanged()
            }



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

//            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
//            rowTransactionsListBinding.root.setOnClickListener {
//                val dataBundle = Bundle()
//                dataBundle.putString("orderDocumentID", recyclerViewList[mainViewHolder.adapterPosition].orderDocumentID)
//                serviceActivity.replaceFragment(ServiceFragmentName.PROCESSING_TRANSACTION_FRAGMENT, true, true, dataBundle)
//            }

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val recyclerViewData = recyclerViewList[position]
            holder.rowTransactionsListBinding.rowTransactionsListViewModel?.textViewTransactionsListText?.value = "판매자 : ${recyclerViewData.sellerDocumentID} <-> 구매자 : ${userNameList[position]}"
            holder.rowTransactionsListBinding.apply{
                root.setOnClickListener {
                    val dataBundle = Bundle().apply {
                        putString("orderDocumentID", recyclerViewData.orderDocumentID)
//                        putString("sellerName", sellerNameList.toString())
//                        putString("userName", userNameList.toString())
//                        putString("pickupName", pickupNameList.toString())
//                        putString("productName", productNameList.toString())
                        //putString("sellerName", sellerNameList[position])
                        putString("sellerName", recyclerViewData.sellerDocumentID)
                        putString("userName", userNameList[position])
                        // putString("pickupName", pickupNameList[position])
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

                    serviceActivity.replaceFragment(ServiceFragmentName.PROCESSING_TRANSACTION_FRAGMENT, true, true, dataBundle)
                }
            }

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