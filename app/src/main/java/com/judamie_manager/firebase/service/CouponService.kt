package com.judamie_manager.firebase.service

import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.repository.CouponRepository
import com.judamie_manager.firebase.vo.CouponVO

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
    }
}