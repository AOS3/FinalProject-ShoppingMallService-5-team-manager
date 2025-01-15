package com.judamie_manager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.databinding.FragmentCouponListBinding
import com.judamie_manager.databinding.FragmentSettingPickupLocationBinding
import com.judamie_manager.databinding.RowCompletedTransactionsListBinding
import com.judamie_manager.databinding.RowSettingPickupLocationBinding
import com.judamie_manager.viewmodel.fragmentviewmodel.CompletedTransactionsListViewModel
import com.judamie_manager.viewmodel.fragmentviewmodel.SettingPickupLocationViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowCompletedTransactionsListViewModel
import com.judamie_manager.viewmodel.rowviewmodel.RowSettingPickupLocationViewModel

class SettingPickupLocationFragment : Fragment() {

    lateinit var fragmentSettingPickupLocationBinding: FragmentSettingPickupLocationBinding
    lateinit var serviceActivity: ServiceActivity

    // ReyclerView 구성을 위한 임시 데이터
    var tempList = Array(100) {
        "픽업지 ${it + 1}"
    }

    // 리사이클러뷰 체크박스 상태 관리
    val checkBoxStates = MutableLiveData<MutableList<Boolean>>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentSettingPickupLocationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_pickup_location, container, false)
        fragmentSettingPickupLocationBinding.settingPickupLocationViewModel = SettingPickupLocationViewModel(this@SettingPickupLocationFragment)
        fragmentSettingPickupLocationBinding.lifecycleOwner = this@SettingPickupLocationFragment

        serviceActivity = activity as ServiceActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()

        // 체크박스 상태 초기화
        checkBoxStates.value = MutableList(tempList.size) { false }

        return fragmentSettingPickupLocationBinding.root
    }

    // 전체 선택 시 모든 체크박스를 업데이트하는 메서드
    fun updateAllCheckBoxes(isChecked: Boolean) {
        // checkBoxStates를 업데이트
        checkBoxStates.value = MutableList(tempList.size) { isChecked }

        // 어댑터에 데이터 변경 알림
        (fragmentSettingPickupLocationBinding.recyclerViewPickupLocation.adapter as RecyclerViewAdapter).notifyDataSetChanged()
    }

    // 리사이클러뷰 구성 메서드
    fun settingRecyclerView() {
        fragmentSettingPickupLocationBinding.apply {
            // RecyclerView에 어댑터를 설정한다.
            recyclerViewPickupLocation.adapter = RecyclerViewAdapter()
            // 한줄에 하나씩 배치한다
            recyclerViewPickupLocation.layoutManager = LinearLayoutManager(serviceActivity)
            val deco = MaterialDividerItemDecoration(serviceActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewPickupLocation.addItemDecoration(deco)
        }
    }

    // 메인 RecyclerView의 어뎁터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>(){
        inner class MainViewHolder(val rowSettingPickupLocationBinding: RowSettingPickupLocationBinding) : RecyclerView.ViewHolder(rowSettingPickupLocationBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowSettingPickupLocationBinding = DataBindingUtil.inflate<RowSettingPickupLocationBinding>(layoutInflater, R.layout.row_setting_pickup_location, parent, false)
            rowSettingPickupLocationBinding.rowSettingPickupLocationViewModel = RowSettingPickupLocationViewModel(this@SettingPickupLocationFragment)
            rowSettingPickupLocationBinding.lifecycleOwner = this@SettingPickupLocationFragment

            val mainViewHolder = MainViewHolder(rowSettingPickupLocationBinding)

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return tempList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.rowSettingPickupLocationBinding.rowSettingPickupLocationViewModel?.textViewPickupNameText?.value = tempList[position]
            holder.rowSettingPickupLocationBinding.rowSettingPickupLocationViewModel?.checkBoxPickupChecked?.value = false
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        serviceActivity.removeFragment(ServiceFragmentName.SETTING_PICKUP_LOCATION)
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentSettingPickupLocationBinding.apply {
            toolbarMain7.title = "픽업지 관리"
            toolbarMain7.inflateMenu(R.menu.menu_set_pickup_toolbar)
            toolbarMain7.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.showPickupMap ->{
                        // 픽업지 지도 보기 화면으로 이동
                        serviceActivity.replaceFragment(ServiceFragmentName.PICKUP_GOOGLE_MAP_FRAGMENT, true, true, null)
                    }
                }
                true
            }
        }
    }

    // FAB 누르면 실행되는 메서드
    fun settingFabAddPickup(){
        // 픽업지 등록 화면으로 이동한다.
        serviceActivity.replaceFragment(ServiceFragmentName.ADD_PICKUP_LOCATION_FRAGMENT, true, true, null)
    }

    // 선택 삭제 버튼 메서드
    fun selectionDelete() {

    }

}