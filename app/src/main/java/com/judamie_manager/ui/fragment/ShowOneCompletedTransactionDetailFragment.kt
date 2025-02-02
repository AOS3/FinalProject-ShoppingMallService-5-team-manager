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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ShowOneCompletedTransactionDetailFragment : Fragment() {

    lateinit var fragmentShowOneCompletedTransactionDetailBinding: FragmentShowOneCompletedTransactionDetailBinding
    lateinit var serviceActivity: ServiceActivity


    // 데이터 번들로 가져온 걸 담을 변수들
    lateinit var orderDocumentID: String
    lateinit var sellerName: String
    lateinit var pickupName: String
    lateinit var userName: String
    lateinit var productName: String
    lateinit var orderTime: String
    lateinit var productPrice: String
    lateinit var productDiscountRate: String
    lateinit var orderCount: String
    lateinit var orderPriceAmount: String
    lateinit var orderState: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentShowOneCompletedTransactionDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_one_completed_transaction_detail,
            container,
            false
        )
        fragmentShowOneCompletedTransactionDetailBinding.showOneCompletedTransactionDetailViewModel =
            ShowOneCompletedTransactionDetailViewModel(this@ShowOneCompletedTransactionDetailFragment)
        fragmentShowOneCompletedTransactionDetailBinding.lifecycleOwner =
            this@ShowOneCompletedTransactionDetailFragment

        serviceActivity = activity as ServiceActivity

        // arguments의 값을 변수에 담아주는 메서드를 호출한다.
        gettingArguments()
        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 상세 거래 내역 텍스트뷰에 값 넣는 메서드 호출
        settingTransactionTextView()

        return fragmentShowOneCompletedTransactionDetailBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
        serviceActivity.removeFragment(ServiceFragmentName.SHOW_ONE_COMPLETED_TRANSACTION_DETAIL_FRAGMENT)
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentShowOneCompletedTransactionDetailBinding.showOneCompletedTransactionDetailViewModel?.toolbarMain4Title?.value =
            "상세거래내역"
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments() {
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
    fun settingTransactionTextView() {
        fragmentShowOneCompletedTransactionDetailBinding.showOneCompletedTransactionDetailViewModel?.apply {

            // 거래 완료 시간을 포맷팅하여 UI에 반영
            val depositTime = serviceActivity.transactionFinishTimes[orderDocumentID]
//            val depositDate =
//                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(depositTime))
            val depositDate = if (depositTime != null) {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(depositTime))
            } else {
                ""
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // 원하는 형식 지정
            val formattedDate = sdf.format(Date(orderTime.toLong()))

            // LiveData에 데이터 세팅
            textViewSellerText.value = "판매자 : $sellerName"
            textViewCustomerText.value = "구매자 : $userName"
            textViewOrderDateText.value = "주문날짜 : ${formattedDate}"
            textViewTransactionDateText.value = "거래날짜 : $depositDate"
            textViewProductText.value = "거래품목 : $productName"
            textViewQuantityText.value = "구매수량 : ${orderCount}개"
            textViewPriceText.value = "개당 가격 : ${productPrice}원"
            textViewTotalPriceText.value = "총 가격 : ${orderPriceAmount}원"
            textViewDeliveryText.value = "배송상태 : 배송 완료"
            textViewPickupText.value = "픽업상태 : 픽업 완료"

        }
    }
}
