package com.judamie_manager.ui.fragment

import android.net.http.SslCertificate.restoreState
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentAddPickupLocationBinding
import com.judamie_manager.ui.component.ConfirmDialogFragment
import com.judamie_manager.viewmodel.fragmentviewmodel.AddPickupLocationViewModel

class AddPickupLocationFragment : Fragment() {

    lateinit var fragmentAddPickupLocationBinding: FragmentAddPickupLocationBinding
    lateinit var serviceActivity: ServiceActivity
    lateinit var addPickupLocationViewModel: AddPickupLocationViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        fragmentAddPickupLocationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_pickup_location, container, false)
        addPickupLocationViewModel = AddPickupLocationViewModel(this@AddPickupLocationFragment)
        fragmentAddPickupLocationBinding.addPickupLocationViewModel = addPickupLocationViewModel
        //fragmentAddPickupLocationBinding.addPickupLocationViewModel = AddPickupLocationViewModel(this@AddPickupLocationFragment)
        // fragmentAddPickupLocationBinding.lifecycleOwner = this@AddPickupLocationFragment
        fragmentAddPickupLocationBinding.lifecycleOwner = viewLifecycleOwner


        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()

        // FragmentResultListener 설정 (주소 결과를 받음)
        parentFragmentManager.setFragmentResultListener("addressRequest", viewLifecycleOwner) { _, result ->
            val addressData = result.getString("address")
            addPickupLocationViewModel.textFieldDolomyeongText.value = addressData
            // fragmentAddPickupLocationBinding.textFieldDolomyeong.editText?.setText(addressData)
        }

        // 상태 복원
        if (savedInstanceState != null) {
            restoreState(savedInstanceState)  // savedInstanceState를 이용한 상태 복원
        }

        return fragmentAddPickupLocationBinding.root
    }

    // 상태 저장
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // 상태 저장
        saveToBundle(outState)  // outState에 데이터를 저장
        Log.d("AddPickupLocationFragment", "onSaveInstanceState: 상태 저장 완료")
    }

    // 상태 복원
    private fun restoreState(bundle: Bundle?) {
        val state = bundle ?: return
        addPickupLocationViewModel.textFieldPickupNameText.value = state.getString("pickupName", "")
        addPickupLocationViewModel.textFieldDolomyeongText.value = state.getString("dolomyeongAddress", "")
        addPickupLocationViewModel.textFieldDetailAddressText.value = state.getString("detailAddress", "")
        addPickupLocationViewModel.textFieldPhoneNumberText.value = state.getString("phoneNumber", "")
        Log.d("AddPickupLocationFragment", "restoreState: 상태 복원 완료")
    }

    // 상태 저장
    private fun saveToBundle(bundle: Bundle) {
        bundle.putString("pickupName", addPickupLocationViewModel.textFieldPickupNameText.value)
        bundle.putString("dolomyeongAddress", addPickupLocationViewModel.textFieldDolomyeongText.value)
        bundle.putString("detailAddress", addPickupLocationViewModel.textFieldDetailAddressText.value)
        bundle.putString("phoneNumber", addPickupLocationViewModel.textFieldPhoneNumberText.value)
        Log.d("AddPickupLocationFragment", "saveToBundle: 데이터 저장 완료")
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        // ViewModel의 데이터를 초기화
        addPickupLocationViewModel.resetData()

        serviceActivity.removeFragment(ServiceFragmentName.ADD_PICKUP_LOCATION_FRAGMENT)
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentAddPickupLocationBinding.apply {
            toolbarMain8.title = "픽업지 등록"
        }
    }

    fun setupDolomyeongApi() {
        serviceActivity.replaceFragment(ServiceFragmentName.DAUM_API_FRAGMENT, true, true, null)
    }


    // 픽업지 등록하기 버튼 눌렀을 때의 메서드
    fun registerPickup(){
        // 파이어베이스에 등록,,, 후에 구현

        // 입력 안한 내용 있나 확인 + 모델에 저장(나중에,,,)
        fragmentAddPickupLocationBinding.apply {
            // 픽업지 이름
            var pickupName = addPickupLocationViewModel?.textFieldPickupNameText?.value!!
            // 도로명 주소
            var dolomyeongAddress = addPickupLocationViewModel?.textFieldDolomyeongText?.value!!
            // 상세 주소
            var detailAddress = addPickupLocationViewModel?.textFieldDetailAddressText?.value!!
            // 전화번호
            var phoneNumber = addPickupLocationViewModel?.textFieldPhoneNumberText?.value!!

            var isValid = true

            // 픽업지 이름 체크
            if (pickupName.isEmpty()) {
                textFieldPickupName.error = "픽업지 이름이 입력되지 않았습니다."
                textFieldPickupName.editText?.requestFocus()
                isValid = false
            } else {
                textFieldPickupName.error = null
            }

            // 도로명 주소 체크
            if (dolomyeongAddress.isEmpty()) {
                textFieldDolomyeong.error = "도로명주소가 입력되지 않았습니다."
                textFieldDolomyeong.editText?.requestFocus()
                isValid = false
            } else {
                textFieldDolomyeong.error = null
            }

            // 상세 주소 체크
            if (detailAddress.isEmpty()) {
                textFieldDetailAddress.error = "상세주소가 입력되지 않았습니다."
                textFieldDetailAddress.editText?.requestFocus()
                isValid = false
            } else {
                textFieldDetailAddress.error = null
            }

            // 전화번호 체크
            if (phoneNumber.isEmpty()) {
                textFieldPhoneNumber.error = "전화번호가 입력되지 않았습니다."
                textFieldPhoneNumber.editText?.requestFocus()
                isValid = false
            } else {
                textFieldPhoneNumber.error = null
            }

            // 모두 입력했을 시 픽업지 등록하고(나중에 수정,,,)
            if (isValid) {
                // 픽업지 등록하기,,,

                // 등록다되면 픽업지 목록화면으로 이동
                movePrevFragment()
            }
        }
    }
}
