package com.judamie_manager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentProcessingTransactionBinding
import com.judamie_manager.databinding.FragmentShowOneCompletedTransactionDetailBinding
import com.judamie_manager.viewmodel.fragmentviewmodel.MainViewModel
import com.judamie_manager.viewmodel.fragmentviewmodel.ProcessingTransactionViewModel

class ProcessingTransactionFragment : Fragment() {
    lateinit var fragmentProcessingTransactionDetailBinding: FragmentProcessingTransactionBinding
    lateinit var serviceActivity: ServiceActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentProcessingTransactionDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_processing_transaction, container, false)
        fragmentProcessingTransactionDetailBinding.processingTransactionViewModel = ProcessingTransactionViewModel(this@ProcessingTransactionFragment)
        fragmentProcessingTransactionDetailBinding.lifecycleOwner = this@ProcessingTransactionFragment

        serviceActivity = activity as ServiceActivity

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

    // 상세 거래 내역 텍스트뷰에 값 넣기
    fun settingTransactionTextView(){
        fragmentProcessingTransactionDetailBinding.processingTransactionViewModel?.apply{
            // 임시 데이터들
            var seller = "김멋사"
            var customer = "ㅌㅌ스토어"
            var orderDate = "2025년 01월 01일"
            var product = "ㅌㅌ하이볼"
            var quantity = 10
            var pricePerUnit = 5000
            var totalPrice = quantity * pricePerUnit
            var deliveryStatus = "배송완료"
            var pickupStatus = "픽업대기"

            // LiveData에 데이터 세팅
            textViewSellerText.value = "판매자 : $seller"
            textViewCustomerText.value = "구매자 : $customer"
            textViewOrderDateText.value = "주문날짜 : $orderDate"
            textViewProductText.value = "거래품목 : $product"
            textViewQuantityText.value = "구매수량 : $quantity"
            textViewPriceText.value = "개당 가격 : $pricePerUnit"
            textViewTotalPriceText.value = "총 가격 : $totalPrice"
            textViewDeliveryText.value = "배송상태 : $deliveryStatus"
            textViewPickupText.value = "픽업상태 : $pickupStatus"

        }
    }

    // 판매자에게 입금하기 버튼 클릭 시 메서드
    fun buttonDeposit(){
        // 버튼 클릭하면 판매자에게 입금처리하기(후에 구현)

        // 입금 처리 완료 되면 스낵바 띄우기
        view?.let { Snackbar.make(it, "입금처리가 완료되었습니다", Snackbar.LENGTH_SHORT).show() }
        // 없애도 됨,,
        serviceActivity.removeFragment(ServiceFragmentName.PROCESSING_TRANSACTION_FRAGMENT)
    }
}