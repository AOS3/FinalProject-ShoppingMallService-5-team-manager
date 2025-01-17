package com.judamie_manager.ui.fragment

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.ParseException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.databinding.FragmentAddCouponBinding
import com.judamie_manager.databinding.FragmentCouponListBinding
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

    // 쿠폰 추가 완료
    fun registerCoupon(){
        fragmentAddCouponBinding.apply {
            // 쿠폰명
            var couponName = addCouponViewModel?.textFieldAddCouponNameText?.value!!
            // 쿠폰 할인율
            var couponSale = addCouponViewModel?.textFieldAddCouponSaleText?.value!!
            // 쿠폰 사용 기한
            var couponDate = addCouponViewModel?.textFieldAddCouponDateText?.value!!

            var isValid = true

            // 쿠폰명 체크
            if (couponName.isEmpty()) {
                textFieldCouponName.error = "쿠폰명을 입력해주세요."
                // textFieldCouponName.editText?.requestFocus()
                serviceActivity.showSoftInput(textFieldCouponName.editText!!)
                isValid = false
            } else {
                textFieldCouponName.error = null
            }

            // 쿠폰 할인율 체크
            if (couponSale.isEmpty()) {
                textFieldCouponSale.error = "할인율을 입력해주세요."
                // textFieldCouponSale.editText?.requestFocus()
                serviceActivity.showSoftInput(textFieldCouponSale.editText!!)
                isValid = false
            } else {
                val sale = couponSale.toIntOrNull()
                if (sale == null || sale < 0 || sale > 100) {
                    textFieldCouponSale.error = "0에서 100 사이의 숫자를 입력해주세요."
                    serviceActivity.showSoftInput(textFieldCouponSale.editText!!)
                    isValid = false
                } else {
                    textFieldCouponSale.error = null
                }
            }

            // 쿠폰 사용 기한 체크
            if (couponDate.isEmpty()) {
                textFieldCouponDate.error = "사용기한을 입력해주세요."
                // textFieldCouponDate.editText?.requestFocus()
                isValid = false
            } else {
                try {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val selectedDate = dateFormat.parse(couponDate)

                    val currentDate = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.time

                    if (selectedDate != null && selectedDate.before(currentDate)) {
                        textFieldCouponDate.error = "사용기한은 오늘 이후 날짜여야 합니다."
                        isValid = false
                    } else {
                        textFieldCouponDate.error = null
                    }
                } catch (e: ParseException) {
                    textFieldCouponDate.error = "날짜 형식이 잘못되었습니다. (yyyy-MM-dd)"
                    isValid = false
                }
            }

            // 모두 입력했을 시 쿠폰 등록하고(나중에 수정,,,) 쿠폰 목록 화면으로 이동
            if (isValid) {
                movePrevFragment()
            }
        }
    }
}