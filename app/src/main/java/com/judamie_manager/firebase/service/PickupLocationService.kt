package com.judamie_manager.firebase.service

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.model.PickupLocationModel
import com.judamie_manager.firebase.repository.CouponRepository
import com.judamie_manager.firebase.repository.PickupLocationRepository
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import kotlinx.coroutines.tasks.await

class PickupLocationService {

    companion object{
        // 픽업지를 저장하는 메서드
        // 새롭게 추가된 픽업지의 id를 반환한다.
        suspend fun addPickupLocationData(pickupLocationModel: PickupLocationModel) : String{
            // VO 객체를 생성한다.
            val pickupLocationVO = pickupLocationModel.toPickupLocationVO()
            // 저장한다.
            val documentId = PickupLocationRepository.addPickupLocationData(pickupLocationVO)
            return documentId
        }

        // 픽업지 목록을 가져오는 메서드
        suspend fun gettingPickupLocationList(): MutableList<PickupLocationModel>{
            // 글정보를 가져온다.
            val pickupLocationList = mutableListOf<PickupLocationModel>()
            val resultList = PickupLocationRepository.gettingPickupLocationList()

            resultList.forEach {
                val pickupLocationVO = it["pickupLocationVO"] as PickupLocationVO
                val documentId = it["documentId"] as String
                val pickupLocationModel = pickupLocationVO.toPickupLocationModel(documentId)
                pickupLocationList.add(pickupLocationModel)
            }

            return pickupLocationList
        }

        // 서버에서 픽업지를 삭제한다
        suspend fun deletePickupLocationData(pickupLocDocumentID:String){
            PickupLocationRepository.deletePickupLocationData(pickupLocDocumentID)
        }
    }
}