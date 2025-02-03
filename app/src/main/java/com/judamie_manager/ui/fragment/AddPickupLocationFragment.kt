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
import com.google.android.material.snackbar.Snackbar
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentAddPickupLocationBinding
import com.judamie_manager.firebase.model.PickupLocationModel
import com.judamie_manager.firebase.service.PickupLocationService
import com.judamie_manager.ui.component.ConfirmDialogFragment
import com.judamie_manager.viewmodel.fragmentviewmodel.AddPickupLocationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddPickupLocationFragment : Fragment() {

    lateinit var fragmentAddPickupLocationBinding: FragmentAddPickupLocationBinding
    lateinit var serviceActivity: ServiceActivity
    lateinit var addPickupLocationViewModel: AddPickupLocationViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (::fragmentAddPickupLocationBinding.isInitialized == false) {


            fragmentAddPickupLocationBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_pickup_location,
                container,
                false
            )
            addPickupLocationViewModel = AddPickupLocationViewModel(this@AddPickupLocationFragment)
            fragmentAddPickupLocationBinding.addPickupLocationViewModel = addPickupLocationViewModel
            //fragmentAddPickupLocationBinding.addPickupLocationViewModel = AddPickupLocationViewModel(this@AddPickupLocationFragment)
            // fragmentAddPickupLocationBinding.lifecycleOwner = this@AddPickupLocationFragment
            fragmentAddPickupLocationBinding.lifecycleOwner = viewLifecycleOwner


        }

        // FragmentResultListener 설정 (주소 결과를 받음)
        parentFragmentManager.setFragmentResultListener(
            "addressRequest", viewLifecycleOwner
        ) { _, result ->
            val addressData = result.getString("address")
            addPickupLocationViewModel.textFieldDolomyeongText.value = addressData
            fragmentAddPickupLocationBinding.textFieldDolomyeong.editText?.setText(addressData)
        }


        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()

        return fragmentAddPickupLocationBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("pickupName", addPickupLocationViewModel.textFieldPickupNameText.value)
        // outState.putString("dolomyeongAddress", addPickupLocationViewModel.textFieldDolomyeongText.value)
        outState.putString(
            "detailAddress",
            addPickupLocationViewModel.textFieldDetailAddressText.value
        )
        outState.putString("phoneNumber", addPickupLocationViewModel.textFieldPhoneNumberText.value)
        outState.putString("addInfo", addPickupLocationViewModel.textFieldAdditionalInfoText.value)
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            addPickupLocationViewModel.textFieldPickupNameText.value = it.getString("pickupName")
            // addPickupLocationViewModel.textFieldDolomyeongText.value = it.getString("dolomyeongAddress")
            addPickupLocationViewModel.textFieldDetailAddressText.value =
                it.getString("detailAddress")
            addPickupLocationViewModel.textFieldPhoneNumberText.value = it.getString("phoneNumber")
            addPickupLocationViewModel.textFieldAdditionalInfoText.value = it.getString("addInfo")
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
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
    fun registerPickup() {
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
            // 추가 상세 사항
            var pickupLocInfomation = addPickupLocationViewModel?.textFieldAdditionalInfoText?.value!!
            // 시간
            var pickupLocTimeStamp = System.nanoTime()

            var isValid = true

//            // 픽업지 이름 체크
//            if (pickupName.isEmpty()) {
//                textFieldPickupName.error = "픽업지 이름이 입력되지 않았습니다."
//                textFieldPickupName.editText?.requestFocus()
//                isValid = false
//            } else {
//                textFieldPickupName.error = null
//            }
//
//            // 도로명 주소 체크
//            if (dolomyeongAddress.isEmpty()) {
//                textFieldDolomyeong.error = "도로명주소가 입력되지 않았습니다."
//                textFieldDolomyeong.editText?.requestFocus()
//                isValid = false
//            } else {
//                textFieldDolomyeong.error = null
//            }
//
//            // 상세 주소 체크
//            if (detailAddress.isEmpty()) {
//                textFieldDetailAddress.error = "상세주소가 입력되지 않았습니다."
//                textFieldDetailAddress.editText?.requestFocus()
//                isValid = false
//            } else {
//                textFieldDetailAddress.error = null
//            }
//
//            // 전화번호 체크
//            if (phoneNumber.isEmpty()) {
//                textFieldPhoneNumber.error = "전화번호가 입력되지 않았습니다."
//                textFieldPhoneNumber.editText?.requestFocus()
//                isValid = false
//            } else {
//                textFieldPhoneNumber.error = null
//            }


            // 전화번호 체크
            if (phoneNumber.isEmpty()) {
                textFieldPhoneNumber.error = "전화번호가 입력되지 않았습니다."
                textFieldPhoneNumber.editText?.requestFocus()
                isValid = false
            } else {
                textFieldPhoneNumber.error = null
            }

            // 상세 주소 체크
            if (detailAddress.isEmpty()) {
                textFieldDetailAddress.error = "상세주소가 입력되지 않았습니다."
                textFieldDetailAddress.editText?.requestFocus()
                isValid = false
            } else {
                textFieldDetailAddress.error = null
            }

            // 도로명 주소 체크
            if (dolomyeongAddress.isEmpty()) {
                textFieldDolomyeong.error = "도로명주소가 입력되지 않았습니다."
                textFieldDolomyeong.editText?.requestFocus()
                isValid = false
            } else {
                textFieldDolomyeong.error = null
            }

            // 픽업지 이름 체크
            if (pickupName.isEmpty()) {
                textFieldPickupName.error = "픽업지 이름이 입력되지 않았습니다."
                textFieldPickupName.editText?.requestFocus()
                isValid = false
            } else {
                textFieldPickupName.error = null
            }

            // 모두 입력했을 시 픽업지 등록하고(나중에 수정,,,)
            if (isValid) {
                // 픽업지 업로드
                CoroutineScope(Dispatchers.Main).launch {
                    val pickupLocationModel = PickupLocationModel()
                    pickupLocationModel.pickupLocName = pickupName
                    pickupLocationModel.pickupLocStreetAddress = dolomyeongAddress
                    pickupLocationModel.pickupLocAddressDetail = detailAddress
                    pickupLocationModel.pickupLocPhoneNumber = phoneNumber
                    pickupLocationModel.pickupLocInfomation = pickupLocInfomation
                    pickupLocationModel.pickupLocTimeStamp = pickupLocTimeStamp

                    // 저장한다
                    val work1 = async(Dispatchers.IO){
                        PickupLocationService.addPickupLocationData(pickupLocationModel)
                    }
                    val documentId = work1.await()
                    // 픽업지 문서의 아이디를 전달한다.
                    val dataBundle = Bundle()
                    dataBundle.putString("pickupLocDocumentID", documentId)
                    // 픽업지 등록 완료 되면 스낵바 띄우기
                    view?.let { Snackbar.make(it, "픽업지가 등록되었습니다.", Snackbar.LENGTH_SHORT).show() }
                    serviceActivity.replaceFragment(ServiceFragmentName.SETTING_PICKUP_LOCATION, true, true, dataBundle)
                    movePrevFragment()
                }
            }
        }
    }
}
