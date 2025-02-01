package com.judamie_manager.firebase.repository

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.judamie_manager.firebase.vo.CouponVO
import kotlinx.coroutines.tasks.await
import java.util.Locale

import com.google.firebase.Timestamp
import java.util.*


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

//        // 쿠폰 상태를 변경하는 메서드 (사용기한 지나면 상태를 1 -> 2로 변경)
//        suspend fun updateCouponStatus() {
//            val firestore = FirebaseFirestore.getInstance()
//            val collectionReference = firestore.collection("CouponData")
//
//            val result = collectionReference.get().await()
//
//            val currentDate = Calendar.getInstance().apply {
//                set(Calendar.HOUR_OF_DAY, 0)
//                set(Calendar.MINUTE, 0)
//                set(Calendar.SECOND, 0)
//                set(Calendar.MILLISECOND, 0)
//            }.time
//
//            result.forEach { document ->
//                val couponVO = document.toObject(CouponVO::class.java)
//
//
//                // 사용기한이 오늘 이전이면 상태 변경
//                val couponExpirationDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(couponVO.couponPeriod)
//                if (couponExpirationDate != null && couponExpirationDate.before(currentDate)) {
//                    // 상태 1 -> 2로 업데이트
//                    collectionReference.document(document.id).update("couponState", 2).await()
//
//                    // 로컬 객체의 상태 업데이트
//                    couponVO.couponState = 2
//                    couponVO.toCouponModel(document.id)
//                }
//            }
//        }

        suspend fun updateCouponStatus() {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("CouponData")

            val result = collectionReference.get().await()

            // 오늘 날짜를 Firebase Timestamp로 변환
            val currentTimestamp = Timestamp(Date())

            result.forEach { document ->
                val couponVO = document.toObject(CouponVO::class.java)

                // couponPeriod 필드가 Long 타입일 경우
                val couponExpirationLong = couponVO.couponPeriod // Firebase에 저장된 값 (Long 타입)

                // Long을 Date로 변환 (밀리초를 기준으로)
                // val couponExpirationDate = Date(couponExpirationLong)
                val couponExpirationDate = Date(couponExpirationLong) // 나노초를 밀리초로 변환
                val couponExpirationTimestamp = Timestamp(couponExpirationDate)

                // 사용기한이 오늘 이전이면 상태 변경
                if (couponExpirationTimestamp < currentTimestamp) {
                    collectionReference.document(document.id).update("couponState", 2).await()

                    // 로컬 객체의 상태 업데이트
                    couponVO.couponState = 2
                    couponVO.toCouponModel(document.id)
                }
            }
        }





        // UserData 컬렉션의 모든 사용자 문서에서 userCoupons 필드를 업데이트하는 메서드
        suspend fun updateUserCouponsWithCouponId(couponId: String) {
            val firestore = FirebaseFirestore.getInstance()
            val userCollection = firestore.collection("UserData")

            // 모든 유저 문서 가져오기
            val usersSnapshot = userCollection.get().await()

            // 배치 작업 시작
            val batch = firestore.batch()

            // 각 사용자 문서에 대해 반복
            usersSnapshot.forEach { userDoc ->
                val userRef = userCollection.document(userDoc.id)

                // userCoupons 필드에서 쿠폰 리스트 가져오기
                val currentUserCoupons = userDoc.get("userCoupons") as? List<String> ?: listOf()

                // 새로운 쿠폰 ID 추가
                val updatedCoupons = currentUserCoupons.toMutableList().apply {
                    add(couponId)
                }

                // userCoupons 필드를 업데이트하도록 배치에 추가
                batch.update(userRef, "userCoupons", updatedCoupons)
            }

            // 배치 작업 실행
            batch.commit().await()
        }

    }
}