package com.judamie_manager.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

        // 픽업지 목록을 가져오는 메서드
        suspend fun gettingPickupLocationList(): MutableList<Map<String, *>> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("PickupLocationData")
            // 데이터를 가져온다.
            val result =
                collectionReference.orderBy("pickupLocTimeStamp", Query.Direction.DESCENDING).get()
                    .await()
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 문서의 id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "pickupLocationVO" to it.toObject(PickupLocationVO::class.java)
                )
                resultList.add(map)
            }
            return resultList
        }

        // 서버에서 픽업지를 삭제한다
        suspend fun deletePickupLocationData(pickupLocDocumentID:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("PickupLocationData")
            val documentReference = collectionReference.document(pickupLocDocumentID)
            documentReference.delete().await()
        }
    }
}