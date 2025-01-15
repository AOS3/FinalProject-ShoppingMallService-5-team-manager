package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_manager.map.PickupGoogleMapFragment
import com.judamie_manager.ui.fragment.CouponListFragment

class PickupGoogleMapViewModel(val pickupGoogleMapFragment: PickupGoogleMapFragment) : ViewModel() {

    // toolbarGoogleMap - Title
    val toolbarGoogleMapTitle = MutableLiveData("")

    companion object{

        // toolbarGoogleMap - onNavigationClickGoogleMap
        @JvmStatic
        @BindingAdapter("onNavigationClickGoogleMap")
        fun onNavigationClickGoogleMap(materialToolbar: MaterialToolbar, pickupGoogleMapFragment: PickupGoogleMapFragment){
            materialToolbar.setNavigationOnClickListener {
                pickupGoogleMapFragment.movePrevFragment()
            }
        }
    }
}