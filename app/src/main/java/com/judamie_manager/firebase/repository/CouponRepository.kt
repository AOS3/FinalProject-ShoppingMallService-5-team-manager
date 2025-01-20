package com.judamie_manager.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.firebase.vo.CouponVO
import kotlinx.coroutines.tasks.await

class CouponRepository {

    companion object{
        // 쿠폰을 저장하는 메서드
        // 새롭게 추가된 쿠폰의 id를 반환한다.
        suspend fun addCouponData(couponVO: CouponVO) : String{
            val fireStore = FirebaseFirestore.getInstance()
            val collectionReference = fireStore.collection("CouponData")
            val documentReference = collectionReference.add(couponVO).await()
            return documentReference.id
        }
    }
}