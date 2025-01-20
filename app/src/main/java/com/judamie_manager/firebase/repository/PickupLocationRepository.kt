package com.judamie_manager.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import kotlinx.coroutines.tasks.await

class PickupLocationRepository {

    companion object{
        // 픽업지를 저장하는 메서드
        // 새롭게 추가된 픽업지의 id를 반환한다.
        suspend fun addPickupLocationData(pickupLocationVO: PickupLocationVO) : String{
            val fireStore = FirebaseFirestore.getInstance()
            val collectionReference = fireStore.collection("PickupLocationData")
            val documentReference = collectionReference.add(pickupLocationVO).await()
            return documentReference.id
        }
    }
}