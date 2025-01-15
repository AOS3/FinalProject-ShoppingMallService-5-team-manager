package com.judamie_manager.ui.component

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class AddCouponDatePickerClass(
    private val fragmentManager: FragmentManager,
    private val onDateSelected: (String) -> Unit // 날짜 선택 시 실행할 콜백
) {

    // DatePicker 표시 메서드
    fun showDatePicker() {
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        datePickerBuilder.setTitleText("날짜 선택")

        // 기본 날짜 설정
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2025)
        calendar.set(Calendar.MONTH, 2)
        calendar.set(Calendar.DAY_OF_MONTH, 6)
        datePickerBuilder.setSelection(calendar.timeInMillis)

        val datePicker = datePickerBuilder.build()

        // 날짜 선택 시 콜백 호출
        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = selection

            val year = selectedCalendar.get(Calendar.YEAR)
            val month = selectedCalendar.get(Calendar.MONTH) + 1
            val day = selectedCalendar.get(Calendar.DAY_OF_MONTH)

            // YYYY-MM-DD 형식으로 콜백 실행
            onDateSelected("$year-$month-$day")
        }

        // DatePicker 표시
        datePicker.show(fragmentManager, "date_picker")
    }
}
