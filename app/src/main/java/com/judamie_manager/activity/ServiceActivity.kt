package com.judamie_manager.activity

import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.judamie_manager.R
import com.judamie_manager.databinding.ActivityServiceBinding
import com.judamie_manager.firebase.service.CouponService
import com.judamie_manager.map.PickupGoogleMapFragment
import com.judamie_manager.ui.fragment.AddCouponFragment
import com.judamie_manager.ui.fragment.AddPickupLocationFragment
import com.judamie_manager.ui.fragment.CompletedTransactionsListFragment
import com.judamie_manager.ui.fragment.CouponListFragment
import com.judamie_manager.ui.fragment.DaumApiFragment
import com.judamie_manager.ui.fragment.MainFragment
import com.judamie_manager.ui.fragment.ProcessingTransactionFragment
import com.judamie_manager.ui.fragment.SettingPickupLocationFragment
import com.judamie_manager.ui.fragment.ShowOneCompletedTransactionDetailFragment
import com.judamie_manager.ui.fragment.TransactionsListFragment
import com.judamie_manager.util.ServiceFragmentName
import kotlin.concurrent.thread


class ServiceActivity : AppCompatActivity() {

    lateinit var activityServiceBinding: ActivityServiceBinding


    // 거래 완료 시간을 저장할 맵 (orderDocumentID -> transactionFinishTime)
    val transactionFinishTimes = mutableMapOf<String, Long>()


    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityServiceBinding = DataBindingUtil.setContentView(this@ServiceActivity, R.layout.activity_service)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 첫 프래그먼트를 보여준다.
        replaceFragment(ServiceFragmentName.MAIN_FRAGMENT, false, false, null)
    }

    override fun onStart() {
        super.onStart()

        // 쿠폰 상태 업데이트 작업을 시작합니다.
        CouponService.startCouponStatusUpdateWork(this)
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: ServiceFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){

        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if(newFragment != null){
            oldFragment = newFragment
        }
        // 프래그먼트 객체
        newFragment = when(fragmentName){
            // 게시판 메인 화면
            ServiceFragmentName.MAIN_FRAGMENT -> MainFragment()
            // 쿠폰 목록 화면
            ServiceFragmentName.COUPON_LIST_FRAGMENT -> CouponListFragment()
            // 쿠폰 추가 화면
            ServiceFragmentName.ADD_COUPON_FRAGMENT -> AddCouponFragment()
            // 거래 완료 내역 목록 화면
            ServiceFragmentName.COMPLETED_TRANSACTIONS_LIST_FRAGMENT -> CompletedTransactionsListFragment()
            // 거래 완료 내역 상세 보기 화면
            ServiceFragmentName.SHOW_ONE_COMPLETED_TRANSACTION_DETAIL_FRAGMENT -> ShowOneCompletedTransactionDetailFragment()
            // 거래 처리 중인 내역 목록 보기 화면
            ServiceFragmentName.TRANSACTIONS_LIST_FRAGMENT -> TransactionsListFragment()
            // 거래 처리 중 내역 상세 보기 화면
            ServiceFragmentName.PROCESSING_TRANSACTION_FRAGMENT -> ProcessingTransactionFragment()
            // 픽업지 관리 화면
            ServiceFragmentName.SETTING_PICKUP_LOCATION -> SettingPickupLocationFragment()
            // 픽업지 추가 화면
            ServiceFragmentName.ADD_PICKUP_LOCATION_FRAGMENT -> AddPickupLocationFragment()
            // 다음 API 화면
            ServiceFragmentName.DAUM_API_FRAGMENT -> DaumApiFragment()
            // 픽업지 구글 지도 화면
            ServiceFragmentName.PICKUP_GOOGLE_MAP_FRAGMENT -> PickupGoogleMapFragment()
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment?.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit{

            if(animate) {
                // 만약 이전 프래그먼트가 있다면
                if(oldFragment != null){
                    oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                    oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                }

                newFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerViewService, newFragment!!)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

//    // 거래 시간 업데이트 메서드
//    fun updateTransactionFinishTime(time: Long) {
//        transactionFinishTime = time
//    }
//
//    // 거래 시간 업데이트한거 초기화
//    fun nullTransactionFinishTime() {
//        transactionFinishTime = 0L
//    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: ServiceFragmentName){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // 키보드 올리는 메서드
    fun showSoftInput(view: View){
        // 입력을 관리하는 매니저
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // 포커스를 준다.
        view.requestFocus()

        thread {
            SystemClock.sleep(500)
            // 키보드를 올린다.
            inputManager.showSoftInput(view, 0)
        }
    }

    // 키보드를 내리는 메서드
    fun hideSoftInput(){
        // 포커스가 있는 뷰가 있다면
        if(currentFocus != null){
            // 입력을 관리하는 매니저
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            // 키보드를 내린다.
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            // 포커스를 해제한다.
            currentFocus?.clearFocus()
        }
    }

    // Activity에서 터치가 발생하면 호출되는 메서드
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 만약 포커스가 주어진 View가 있다면
        if(currentFocus != null){
            // 현재 포커스가 주어진 View의 화면상의 영역 정보를 가져온다.
            val rect = Rect()
            currentFocus?.getGlobalVisibleRect(rect)
            // 현재 터치 지점이 포커스를 가지고 있는 View의 영역 내부가 아니라면
            if(rect.contains(ev?.x?.toInt()!!, ev?.y?.toInt()!!) == false){
                // 키보드를 내리고 포커스를 제거한다.
                hideSoftInput()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
