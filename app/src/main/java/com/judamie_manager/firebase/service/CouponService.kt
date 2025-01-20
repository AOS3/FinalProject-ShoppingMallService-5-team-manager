package com.judamie_manager.firebase.service

import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.repository.CouponRepository
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.ui.fragment.CouponListFragment

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

        // 쿠폰 목록을 가져오는 메서드
        suspend fun gettingCouponList(): MutableList<CouponModel>{
            // 글정보를 가져온다.
            val couponList = mutableListOf<CouponModel>()
            val resultList = CouponRepository.gettingCouponList()

            resultList.forEach {
                val couponVO = it["couponVO"] as CouponVO
                val documentId = it["documentId"] as String
                val couponModel = couponVO.toCouponModel(documentId)
                couponList.add(couponModel)
            }

            return couponList
        }
    }
}