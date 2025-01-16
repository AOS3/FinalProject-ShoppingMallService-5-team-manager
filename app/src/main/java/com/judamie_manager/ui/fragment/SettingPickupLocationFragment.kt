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

        // checkBoxStates 초기화
        checkBoxStates.value = MutableList(tempList.size) { false }

        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 리사이클러뷰 구성 메서드 호출
        settingRecyclerView()

        return fragmentSettingPickupLocationBinding.root
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
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>() {

        inner class MainViewHolder(val rowSettingPickupLocationBinding: RowSettingPickupLocationBinding) :
            RecyclerView.ViewHolder(rowSettingPickupLocationBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowSettingPickupLocationBinding =
                DataBindingUtil.inflate<RowSettingPickupLocationBinding>(
                    layoutInflater,
                    R.layout.row_setting_pickup_location,
                    parent,
                    false
                )
            rowSettingPickupLocationBinding.rowSettingPickupLocationViewModel =
                RowSettingPickupLocationViewModel(this@SettingPickupLocationFragment)
            rowSettingPickupLocationBinding.lifecycleOwner = this@SettingPickupLocationFragment

            val mainViewHolder = MainViewHolder(rowSettingPickupLocationBinding)

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return tempList.size
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val rowViewModel = holder.rowSettingPickupLocationBinding.rowSettingPickupLocationViewModel!!
            rowViewModel.textViewPickupNameText.value = tempList[position]

            // 체크박스 상태 바인딩
            val isChecked = checkBoxStates.value?.get(position) ?: false
            rowViewModel.checkBoxPickupChecked.value = isChecked

            holder.rowSettingPickupLocationBinding.checkBoxPickup.setOnCheckedChangeListener { _, newState ->
                checkBoxStates.value?.let {
                    it[position] = newState
                    checkBoxStates.value = it
                }
            }
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
        // 체크된 항목만 필터링하여 삭제
        val updatedList = mutableListOf<String>()
        val updatedStates = mutableListOf<Boolean>()

        // tempList와 checkBoxStates를 순회하면서, 체크된 항목만 새로운 리스트에 추가
        for (i in tempList.indices) {
            if (checkBoxStates.value?.get(i) == true) {
                // 체크된 항목은 삭제하지 않음 (스킵)
            } else {
                // 체크되지 않은 항목만 새로운 리스트에 추가
                updatedList.add(tempList[i])
                updatedStates.add(false) // 새로운 리스트의 체크박스 상태는 모두 'false'
            }
        }

        // 변경된 리스트와 체크박스 상태 반영
        tempList = updatedList.toTypedArray()
        checkBoxStates.value = updatedStates

        // RecyclerView 어댑터에 데이터 변경을 알리기
        fragmentSettingPickupLocationBinding.recyclerViewPickupLocation.adapter?.notifyDataSetChanged()
    }


    // 전체 선택 시 모든 체크박스를 업데이트하는 메서드
    fun updateAllCheckBoxes(isChecked: Boolean) {
        // 전체 선택 상태에 맞게 모든 체크박스 상태를 갱신
        val updatedStates = MutableList(tempList.size) { isChecked }

        // checkBoxStates의 상태를 업데이트
        checkBoxStates.value = updatedStates

        // 전체 선택 상태를 반영하기 위해 ViewModel에서 관리하는 checkBoxPickupAllChecked 상태 업데이트
        (fragmentSettingPickupLocationBinding.settingPickupLocationViewModel?.checkBoxPickupAllChecked)?.value = isChecked

        // RecyclerView 어댑터에 데이터 변경을 알리기
        fragmentSettingPickupLocationBinding.recyclerViewPickupLocation.adapter?.notifyDataSetChanged()
    }

}