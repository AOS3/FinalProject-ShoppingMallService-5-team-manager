package com.judamie_manager.firebase.repository

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.judamie_manager.firebase.vo.CouponVO
import kotlinx.coroutines.tasks.await
import java.util.Locale

class CouponRepository {

    companion object {
        // 쿠폰을 저장하는 메서드
        // 새롭게 추가된 쿠폰의 id를 반환한다.
        suspend fun addCouponData(couponVO: CouponVO): String {
            val fireStore = FirebaseFirestore.getInstance()
            val collectionReference = fireStore.collection("CouponData")
            val documentReference = collectionReference.add(couponVO).await()
            return documentReference.id
        }

        // 쿠폰 목록을 가져오는 메서드
        suspend fun gettingCouponList(): MutableList<Map<String, *>> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("CouponData")
            // 데이터를 가져온다.
            val result =
                collectionReference.orderBy("couponTimeStamp", Query.Direction.DESCENDING).get()
                    .await()
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 문서의 id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "couponVO" to it.toObject(CouponVO::class.java)
                )
                resultList.add(map)
            }
            return resultList
        }

        // 쿠폰 상태를 변경하는 메서드 (사용기한 지나면 상태를 1 -> 2로 변경)
        suspend fun updateCouponStatus() {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("CouponData")

            val result = collectionReference.get().await()

            val currentDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            result.forEach { document ->
                val couponVO = document.toObject(CouponVO::class.java)

                // 사용기한이 오늘 이전이면 상태 변경
                val couponExpirationDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(couponVO.couponPeriod)
                if (couponExpirationDate != null && couponExpirationDate.before(currentDate)) {
                    // 상태 1 -> 2로 업데이트
                    collectionReference.document(document.id).update("couponState", 2).await()

                    // 로컬 객체의 상태 업데이트
                    couponVO.couponState = 2
                    couponVO.toCouponModel(document.id)
                }
            }
        }


    }
}