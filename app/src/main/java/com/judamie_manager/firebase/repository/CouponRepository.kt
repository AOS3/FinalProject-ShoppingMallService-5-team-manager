package com.judamie_manager.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.judamie_manager.firebase.vo.CouponVO
import kotlinx.coroutines.tasks.await

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
    }
}