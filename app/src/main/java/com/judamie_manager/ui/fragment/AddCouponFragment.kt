package com.judamie_manager.ui.fragment

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.ParseException
import android.os.BugreportManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.databinding.FragmentAddCouponBinding
import com.judamie_manager.databinding.FragmentCouponListBinding
import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.repository.CouponRepository
import com.judamie_manager.firebase.service.CouponService
import com.judamie_manager.ui.component.AddCouponDatePickerClass
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.viewmodel.fragmentviewmodel.AddCouponViewModel
import com.judamie_manager.viewmodel.fragmentviewmodel.CouponListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Locale

class AddCouponFragment : Fragment() {

    lateinit var fragmentAddCouponBinding: FragmentAddCouponBinding
    lateinit var serviceActivity: ServiceActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentAddCouponBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_coupon, container, false)
        fragmentAddCouponBinding.addCouponViewModel = AddCouponViewModel(this@AddCouponFragment)
        fragmentAddCouponBinding.lifecycleOwner = this@AddCouponFragment

        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()

        return fragmentAddCouponBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentAddCouponBinding.apply {
            toolbarMain2.title = "쿠폰 관리"
            toolbarMain2.inflateMenu(R.menu.menu_add_coupon_toolbar)
            toolbarMain2.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.memuItemAddCoupon ->{
                        // 쿠폰 추가 완료
                        registerCoupon()
                    }
                }
                true
            }

        }
    }

    // 사용기한 필드에 날짜 선택 기능 추가
    fun setupDatePicker() {
        val datePicker = AddCouponDatePickerClass(requireActivity().supportFragmentManager) { selectedDate ->
            fragmentAddCouponBinding.textFieldCouponDate.editText?.setText(selectedDate)
        }
        datePicker.showDatePicker()
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        serviceActivity.removeFragment(ServiceFragmentName.ADD_COUPON_FRAGMENT)
    }

    // 쿠폰 추가 완료 처리 메서드
    fun registerCoupon(){
        fragmentAddCouponBinding.apply {
            // 쿠폰명
            var couponName = addCouponViewModel?.textFieldAddCouponNameText?.value!!
            // 쿠폰 할인율
            var couponDiscountRate = addCouponViewModel?.textFieldAddCouponSaleText?.value!!
            // 쿠폰 사용 기한
            // 쿠폰 사용 기한을 Timestamp로 변환
            val dateString = addCouponViewModel?.textFieldAddCouponDateText?.value!!
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            var couponPeriod: Timestamp? = null

            // 날짜 문자열이 비어 있지 않으면 변환 시도
            if (dateString.isNotEmpty()) {
                try {
                    // 문자열을 Date로 변환 후 Timestamp로 변환
                    val date = dateFormat.parse(dateString)
                    couponPeriod = date?.let { Timestamp(it) }
                } catch (e: ParseException) {
                    // 날짜 파싱 오류 처리 (예: 사용자에게 오류 메시지 표시)
                    textFieldCouponDate.error = "유효한 날짜 형식이 아닙니다."
                    serviceActivity.showSoftInput(textFieldCouponDate.editText!!)
                    var isValid = false
                }
            } else {
                // 날짜가 비어 있으면 현재 시간을 기본 값으로 사용
                couponPeriod = Timestamp.now()
            }

            // 시간
            var couponTimeStamp = System.nanoTime()

            var isValid = true

//            // 쿠폰명 체크
//            if (couponName.isEmpty()) {
//                textFieldCouponName.error = "쿠폰명을 입력해주세요."
//                // textFieldCouponName.editText?.requestFocus()
//                serviceActivity.showSoftInput(textFieldCouponName.editText!!)
//                isValid = false
//            } else {
//                textFieldCouponName.error = null
//            }
//
//            // 쿠폰 할인율 체크
//            if (couponDiscountRate.isEmpty()) {
//                textFieldCouponSale.error = "할인율을 입력해주세요."
//                // textFieldCouponSale.editText?.requestFocus()
//                serviceActivity.showSoftInput(textFieldCouponSale.editText!!)
//                isValid = false
//            } else {
//                val sale = couponDiscountRate.toIntOrNull()
//                if (sale == null || sale < 0 || sale > 100) {
//                    textFieldCouponSale.error = "0에서 100 사이의 숫자를 입력해주세요."
//                    serviceActivity.showSoftInput(textFieldCouponSale.editText!!)
//                    isValid = false
//                } else {
//                    textFieldCouponSale.error = null
//                }
//            }
//
//            // 쿠폰 사용 기한 체크
//            if (couponPeriod == null) {
//                textFieldCouponDate.error = "사용기한을 입력해주세요."
//                isValid = false
//            } else {
//                val selectedDate = couponPeriod.toDate() // Timestamp -> Date 변환
//
//                val currentDate = Calendar.getInstance().apply {
//                    set(Calendar.HOUR_OF_DAY, 0)
//                    set(Calendar.MINUTE, 0)
//                    set(Calendar.SECOND, 0)
//                    set(Calendar.MILLISECOND, 0)
//                }.time
//
//                if (selectedDate.before(currentDate)) {
//                    textFieldCouponDate.error = "사용기한은 오늘 이후 날짜여야 합니다."
//                    isValid = false
//                } else {
//                    textFieldCouponDate.error = null
//                }
//            }

            // 쿠폰명 체크
            if (couponName.isEmpty()) {
                textFieldCouponName.error = "쿠폰명을 입력해주세요."
                serviceActivity.showSoftInput(textFieldCouponName.editText!!)
                isValid = false
            } else {
                textFieldCouponName.error = null

                // 쿠폰 할인율 체크 (쿠폰명이 유효한 경우에만)
                if (couponDiscountRate.isEmpty()) {
                    textFieldCouponSale.error = "할인율을 입력해주세요."
                    serviceActivity.showSoftInput(textFieldCouponSale.editText!!)
                    isValid = false
                } else {
                    val sale = couponDiscountRate.toIntOrNull()
                    if (sale == null || sale < 0 || sale > 100) {
                        textFieldCouponSale.error = "0에서 100 사이의 숫자를 입력해주세요."
                        serviceActivity.showSoftInput(textFieldCouponSale.editText!!)
                        isValid = false
                    } else {
                        textFieldCouponSale.error = null

                        // 쿠폰 사용 기한 체크 (할인율이 유효한 경우에만)
                        if (couponPeriod == null) {
                            textFieldCouponDate.error = "사용기한을 입력해주세요."
                            isValid = false
                        } else {
                            val selectedDate = couponPeriod.toDate() // Timestamp -> Date 변환

                            val currentDate = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }.time

                            if (selectedDate.before(currentDate)) {
                                textFieldCouponDate.error = "사용기한은 오늘 이후 날짜여야 합니다."
                                isValid = false
                            } else {
                                textFieldCouponDate.error = null
                            }
                        }
                    }
                }
            }


            // 모두 입력했을 시 쿠폰 등록하고(나중에 수정,,,) 쿠폰 목록 화면으로 이동
            if (isValid) {

                // 업로드
                CoroutineScope(Dispatchers.Main).launch {


                    // 쿠폰 사용 기한을 밀리초 단위로 저장 (Timestamp를 밀리초로 변환)
                    val couponPeriodMillis = couponPeriod?.let {
                        it.seconds * 1000 + it.nanoseconds / 1000000
                    } ?: 0L // couponPeriod가 null이면 기본값으로 0L을 사용


                    // 서버에 저장할 댓글 데이터
                    val couponModel = CouponModel()
                    couponModel.couponName = couponName
                    couponModel.couponDiscountRate = couponDiscountRate.toInt()
                    couponModel.couponPeriod = couponPeriodMillis
                    couponModel.couponTimeStamp = couponTimeStamp

                    // 저장한다.
                    val work1 = async(Dispatchers.IO) {
                        CouponService.addCouponData(couponModel)
                    }
                    val documentId = work1.await()

                    // 쿠폰의 아이디를 전달한다.
                    val dataBundle = Bundle().apply {
                        putString("couponDocumentID", documentId)
                    }

                    CouponRepository.updateUserCouponsWithCouponId(documentId)

                    // 쿠폰 등록 완료되면 스낵바 띄우기
                    view?.let { Snackbar.make(it, "쿠폰이 등록되었습니다.", Snackbar.LENGTH_SHORT).show() }
                    serviceActivity.replaceFragment(ServiceFragmentName.COUPON_LIST_FRAGMENT, true, true, dataBundle)
                    movePrevFragment()
                }
            }

        }
    }
}