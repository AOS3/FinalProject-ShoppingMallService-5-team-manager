package com.judamie_manager.ui.component

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.judamie_manager.R
import com.judamie_manager.databinding.FragmentConfirmDialogBinding
import com.judamie_manager.viewmodel.componentviewmodel.ConfirmDialogViewModel


class ConfirmDialogFragment(
    private val title: String?,
    private val content: String?,
    private val phoneNumber: String
) : DialogFragment() {

    private lateinit var fragmentConfirmDialogBinding: FragmentConfirmDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 데이터 바인딩 설정
        fragmentConfirmDialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_dialog, container, false)
        fragmentConfirmDialogBinding.confirmDialogViewModel = ConfirmDialogViewModel(this@ConfirmDialogFragment)
        fragmentConfirmDialogBinding.lifecycleOwner = this

        // 다이얼로그 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 데이터 바인딩을 통해 뷰와 데이터 연결
        fragmentConfirmDialogBinding.confirmDialogViewModel?.apply {

            pickupLocationNameText.value = title

            if (content.isNullOrEmpty()) {
                pickupLocationDetailTextVisibility.value = View.GONE
            } else {
                pickupLocationDetailText.value = content
            }
        }

        return fragmentConfirmDialogBinding.root
    }

    // 다이얼로그 닫기 버튼 메서드
    fun buttonClose(){
        dismiss()
    }

    // 전화 걸기 버튼 메서드
    fun buttonCalling() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }
}
