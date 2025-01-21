package com.judamie_manager.firebase.service

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.repository.CouponRepository
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.worker.CouponStatusUpdateWorker
import com.judamie_manager.ui.fragment.CouponListFragment
import java.util.concurrent.TimeUnit

class CouponService {

    companion object{
        // 쿠폰을 저장하는 메서드
        // 새롭게 추가된 쿠폰의 id를 반환한다.
        suspend fun addCouponData(couponModel: CouponModel) : String{
            // VO 객체를 생성한다.
            val couponVO = couponModel.toCouponVO()
            // 저장한다.
            val documentId = CouponRepository.addCouponData(couponVO)
            return documentId
        }

        // 쿠폰 목록을 가져오는 메서드
        suspend fun gettingCouponList(): MutableList<CouponModel>{
            // 글정보를 가져온다.
            val couponList = mutableListOf<CouponModel>()
            val resultList = CouponRepository.gettingCouponList()

            resultList.forEach {
                val couponVO = it["couponVO"] as CouponVO
                val documentId = it["documentId"] as String
                val couponModel = couponVO.toCouponModel(documentId)
                couponList.add(couponModel)
            }

            return couponList
        }

        // 쿠폰 상태를 업데이트 (하루에 한번 주기적으로 실행)
        fun startCouponStatusUpdateWork(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<CouponStatusUpdateWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(1, TimeUnit.MINUTES) // 앱 시작 후 1분 뒤 실행 (테스트 용)
                .build()

            // WorkManager에 작업 예약
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}