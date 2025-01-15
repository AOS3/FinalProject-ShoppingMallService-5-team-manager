package com.judamie_manager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentCouponListBinding
import com.judamie_manager.databinding.FragmentMainBinding
import com.judamie_manager.databinding.RowCouponListBinding
import com.judamie_manager.viewmodel.fragmentviewmodel.CouponListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowCouponListViewModel

class CouponListFragment : Fragment() {

    lateinit var fragmentCouponListBinding: FragmentCouponListBinding
    lateinit var serviceActivity: ServiceActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList1 = Array(100) {
        "쿠폰명 ${it + 1}"
    }
    val tempList2 = Array(100){
        "사용기한 ${it + 1}"
    }

    // 임시 데이터를 이용해 recyclerViewList를 구성
    val recyclerViewList = tempList1.zip(tempList2) { couponName, couponDate ->
        CouponItem(couponName, couponDate)
    }

    // 데이터 클래스를 생성하여 couponName과 couponDate를 포함
    // 나중엔 var recyclerViewList = mutableListOf<CouponModel>() 이런식으로...바꿔..
    data class CouponItem(val couponName: String, val couponDate: String)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentCouponListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_coupon_list, container, false)
        fragmentCouponListBinding.couponListViewModel = CouponListViewModel(this@CouponListFragment)
        fragmentCouponListBinding.lifecycleOwner = this@CouponListFragment

        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()

        return fragmentCouponListBinding.root
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        serviceActivity.removeFragment(ServiceFragmentName.COUPON_LIST_FRAGMENT)
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentCouponListBinding.apply {
            toolbarMain.title = "쿠폰 관리"
            toolbarMain.inflateMenu(R.menu.menu_coupon_list_toolbar)
            toolbarMain.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemCouponList -> {
                        // 쿠폰 추가 화면으로 이동
                        serviceActivity.replaceFragment(
                            ServiceFragmentName.ADD_COUPON_FRAGMENT, true, true, null)
                    }

                }
                true
            }
        }
    }

    // 리사이클러뷰 구성 메서드
    fun settingRecyclerView() {
        fragmentCouponListBinding.apply {
            // RecyclerView에 어댑터를 설정한다.
            recyclerViewCoupon.adapter = RecyclerViewAdapter()
            // 한줄에 하나씩 배치한다
            recyclerViewCoupon.layoutManager = LinearLayoutManager(serviceActivity)
            val deco = MaterialDividerItemDecoration(serviceActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewCoupon.addItemDecoration(deco)
        }
    }

    // 메인 RecyclerView의 어뎁터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>(){
        inner class MainViewHolder(val rowCouponListBinding: RowCouponListBinding) : RecyclerView.ViewHolder(rowCouponListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowCouponListBinding = DataBindingUtil.inflate<RowCouponListBinding>(layoutInflater, R.layout.row_coupon_list, parent, false)
            rowCouponListBinding.rowCouponListViewModel = RowCouponListViewModel(this@CouponListFragment)
            rowCouponListBinding.lifecycleOwner = this@CouponListFragment

            val mainViewHolder = MainViewHolder(rowCouponListBinding)

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowCouponListBinding.rowCouponListViewModel?.textViewCouponNameText?.value = recyclerViewList[position].couponName
            holder.rowCouponListBinding.rowCouponListViewModel?.textViewCouponDateText?.value = recyclerViewList[position].couponDate
        }
    }
}