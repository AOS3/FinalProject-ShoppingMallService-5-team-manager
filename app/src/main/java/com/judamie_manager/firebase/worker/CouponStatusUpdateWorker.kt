package com.judamie_manager.firebase.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.judamie_manager.firebase.repository.CouponRepository

class CouponStatusUpdateWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 쿠폰 상태를 업데이트하는 작업을 수행
            CouponRepository.updateCouponStatus()  // 상태 업데이트 수행
            Result.success()  // 성공적으로 완료
        } catch (e: Exception) {
            // 오류가 발생하면 실패 상태로 반환
            Result.failure()
        }
    }
}
