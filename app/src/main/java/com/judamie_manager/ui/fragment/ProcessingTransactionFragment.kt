package com.judamie_manager.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentProcessingTransactionBinding
import com.judamie_manager.databinding.FragmentShowOneCompletedTransactionDetailBinding
import com.judamie_manager.firebase.repository.OrderRepository
import com.judamie_manager.util.OrderStateType
import com.judamie_manager.viewmodel.fragmentviewmodel.MainViewModel
import com.judamie_manager.viewmodel.fragmentviewmodel.ProcessingTransactionViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ProcessingTransactionFragment : Fragment() {

    lateinit var fragmentProcessingTransactionDetailBinding: FragmentProcessingTransactionBinding
    lateinit var serviceActivity: ServiceActivity




    // 데이터 번들로 가져온 걸 담을 변수들
    lateinit var orderDocumentID:String
    lateinit var sellerName:String
    lateinit var pickupName:String
    lateinit var userName:String
    lateinit var productName:String
    lateinit var orderTime:String
    lateinit var productPrice :String
    lateinit var productDiscountRate:String
    lateinit var orderCount:String
    lateinit var orderPriceAmount:String
    lateinit var orderState:String



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentProcessingTransactionDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_processing_transaction, container, false)
        fragmentProcessingTransactionDetailBinding.processingTransactionViewModel = ProcessingTransactionViewModel(this@ProcessingTransactionFragment)
        fragmentProcessingTransactionDetailBinding.lifecycleOwner = this@ProcessingTransactionFragment

        serviceActivity = activity as ServiceActivity

        // arguments의 값을 변수에 담아주는 메서드를 호출한다.
        gettingArguments()
        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 상세 거래 내역 텍스트뷰에 값 넣는 메서드 호출
        settingTransactionTextView()

        return fragmentProcessingTransactionDetailBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        serviceActivity.removeFragment(ServiceFragmentName.PROCESSING_TRANSACTION_FRAGMENT)
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentProcessingTransactionDetailBinding.processingTransactionViewModel?.toolbarMain6Title?.value = "상세 거래 처리"
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments(){
        orderDocumentID = arguments?.getString("orderDocumentID")!!
        sellerName = arguments?.getString("sellerName")!!
        // pickupName = arguments?.getString("pickupName")!!
        userName = arguments?.getString("userName")!!
        productName = arguments?.getString("productName")!!
        orderTime = arguments?.getString("orderTime")!!
        productPrice = arguments?.getString("productPrice")!!
        productDiscountRate = arguments?.getString("productDiscountRate")!!
        orderCount = arguments?.getString("orderCount")!!
        orderState = arguments?.getString("orderState")!!
        orderPriceAmount = arguments?.getString("orderPriceAmount")!!


    }

    // 상세 거래 내역 텍스트뷰에 값 넣기
    fun settingTransactionTextView(){
        fragmentProcessingTransactionDetailBinding.processingTransactionViewModel?.apply{

            val deliveryStatus : String
            val pickupStatus : String


            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // 원하는 형식 지정
            val formattedDate = sdf.format(Date(orderTime.toLong()))

            // LiveData에 데이터 세팅
            textViewSellerText.value = "판매자 : $sellerName"
            textViewCustomerText.value = "구매자 : $userName"
            textViewOrderDateText.value = "주문날짜 : $formattedDate"
            textViewProductText.value = "거래품목 : $productName"
            textViewQuantityText.value = "구매수량 : ${orderCount}개"
            textViewPriceText.value = "개당 가격 : ${productPrice}원"
            textViewTotalPriceText.value = "총 가격 : ${orderPriceAmount}원"


            when (orderState) {
                OrderStateType.ORDER_STATE_PAYMENT_COMPLETE.toString() -> {
                    deliveryStatus = "배송 대기"
                    pickupStatus = "픽업 대기"
                }
                OrderStateType.ORDER_STATE_DELIVERY.toString() -> {
                    deliveryStatus = "배송 완료"
                    pickupStatus = "픽업 대기"
                }
                OrderStateType.ORDER_STATE_PICKUP_COMPLETED.toString() -> {
                    deliveryStatus = "배송 완료"
                    pickupStatus = "픽업 완료"
                }
                OrderStateType.ORDER_STATE_TRANSFER_COMPLETED.toString() -> {
                    deliveryStatus = "배송 완료"
                    pickupStatus = "픽업 완료"
                }
                else -> {
                    deliveryStatus = "상태 미확인"
                    pickupStatus = "상태 미확인"
                }
            }

            textViewDeliveryText.value = "배송상태 : $deliveryStatus"
            textViewPickupText.value = "픽업상태 : $pickupStatus"

        }
    }

    // 판매자에게 입금하기 버튼 클릭 시 메서드
    fun buttonDeposit(){
        // 버튼 클릭하면 판매자에게 입금처리하기
        if(orderState == OrderStateType.ORDER_STATE_PICKUP_COMPLETED.toString()) {
            OrderRepository.updateOrderState(orderDocumentID)
            // 입금 처리 완료 되면 스낵바 띄우기
            view?.let { Snackbar.make(it, "입금처리가 완료되었습니다", Snackbar.LENGTH_SHORT).show() }
            // 입금 처리 완료 시간을 기록
            val depositTime = System.currentTimeMillis()
            // Activity에 거래 완료 시간 업데이트
            // serviceActivity.updateTransactionFinishTime(depositTime)
            // transactionFinishTimes 맵에 거래 완료 시간 저장
            serviceActivity.transactionFinishTimes[orderDocumentID] = depositTime

            // Firebase Firestore에 orderTransactionTime 업데이트
            val orderRef = FirebaseFirestore.getInstance()
                .collection("OrderData")
                .document(orderDocumentID)

            orderRef.update("orderTransactionTime", depositTime)
                .addOnSuccessListener {
                    Log.d("FirestoreUpdate", "orderTransactionTime 업데이트 성공")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreUpdate", "orderTransactionTime 업데이트 실패", e)
                }

        }else{
            view?.let { Snackbar.make(it, "배송, 픽업 처리가 완료된 후 시도해주세요", Snackbar.LENGTH_SHORT).show() }
        }

    }

}