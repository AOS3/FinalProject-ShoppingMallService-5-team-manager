package com.judamie_manager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentMainBinding
import com.judamie_manager.viewmodel.fragmentviewmodel.CouponListViewModel
import com.judamie_manager.viewmodel.fragmentviewmodel.MainViewModel

class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var serviceActivity: ServiceActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        fragmentMainBinding.mainViewModel = MainViewModel(this@MainFragment)
        fragmentMainBinding.lifecycleOwner = this@MainFragment

        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()

        return fragmentMainBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentMainBinding.apply {
            toolbarMain.title = "메인 화면"
        }
    }

    // 버튼 클릭 이벤트 처리 (수정)
    fun moveToMainCoupon(){
        // 쿠폰 목록 화면으로 이동
        serviceActivity.replaceFragment(ServiceFragmentName.COUPON_LIST_FRAGMENT, true, true, null)
    }

    fun moveToMainTranCompleted() {
        // 거래 완료 내역 목록 화면으로 이동
        serviceActivity.replaceFragment(ServiceFragmentName.COMPLETED_TRANSACTIONS_LIST_FRAGMENT, true, true, null)
    }

    fun moveToMainTranList() {
        // 거래 처리 중인 내역 목록 화면으로 이동
        serviceActivity.replaceFragment(ServiceFragmentName.TRANSACTIONS_LIST_FRAGMENT, true, true, null)
    }

    fun moveToMainCouponPickup() {
        // 픽업지 관리 화면으로 이동
        serviceActivity.replaceFragment(ServiceFragmentName.SETTING_PICKUP_LOCATION, true, true, null)
    }
}
