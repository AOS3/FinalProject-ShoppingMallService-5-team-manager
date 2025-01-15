package com.judamie_manager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentShowOneCompletedTransactionDetailBinding
import com.judamie_manager.viewmodel.fragmentviewmodel.CouponListViewModel
import com.judamie_manager.viewmodel.fragmentviewmodel.ShowOneCompletedTransactionDetailViewModel

class ShowOneCompletedTransactionDetailFragment : Fragment() {

    lateinit var fragmentShowOneCompletedTransactionDetailBinding:FragmentShowOneCompletedTransactionDetailBinding
    lateinit var serviceActivity: ServiceActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentShowOneCompletedTransactionDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_one_completed_transaction_detail, container, false)
        fragmentShowOneCompletedTransactionDetailBinding.showOneCompletedTransactionDetailViewModel = ShowOneCompletedTransactionDetailViewModel(this@ShowOneCompletedTransactionDetailFragment)
        fragmentShowOneCompletedTransactionDetailBinding.lifecycleOwner = this@ShowOneCompletedTransactionDetailFragment

        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 상세 거래 내역 텍스트뷰에 값 넣는 메서드 호출
        settingTransactionTextView()

        return fragmentShowOneCompletedTransactionDetailBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        serviceActivity.removeFragment(ServiceFragmentName.SHOW_ONE_COMPLETED_TRANSACTION_DETAIL_FRAGMENT)
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentShowOneCompletedTransactionDetailBinding.showOneCompletedTransactionDetailViewModel?.toolbarMain4Title?.value = "상세거래내역"
    }

    // 상세 거래 내역 텍스트뷰에 값 넣기
    fun settingTransactionTextView(){
        fragmentShowOneCompletedTransactionDetailBinding.showOneCompletedTransactionDetailViewModel?.apply{

            // 임시 데이터들
            var seller = "김멋사"
            var customer = "ㅌㅌ스토어"
            var orderDate = "2025년 01월 01일"
            var transactionDate = "2025년 01월 02일"
            var product = "ㅌㅌ하이볼"
            var quantity = 10
            var pricePerUnit = 5000
            var totalPrice = quantity * pricePerUnit
            var deliveryStatus = "배송완료"
            var pickupStatus = "픽업완료"

            // LiveData에 데이터 세팅
            textViewSellerText.value = "판매자 : $seller"
            textViewCustomerText.value = "구매자 : $customer"
            textViewOrderDateText.value = "주문날짜 : $orderDate"
            textViewTransactionDateText.value = "거래날짜 : $transactionDate"
            textViewProductText.value = "거래품목 : $product"
            textViewQuantityText.value = "구매수량 : $quantity"
            textViewPriceText.value = "개당 가격 : $pricePerUnit"
            textViewTotalPriceText.value = "총 가격 : $totalPrice"
            textViewDeliveryText.value = "배송상태 : $deliveryStatus"
            textViewPickupText.value = "픽업상태 : $pickupStatus"

        }
    }
}