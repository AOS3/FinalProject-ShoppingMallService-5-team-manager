package com.judamie_manager.firebase.service

import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.model.PickupLocationModel
import com.judamie_manager.firebase.repository.CouponRepository
import com.judamie_manager.firebase.repository.PickupLocationRepository

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
    }
}