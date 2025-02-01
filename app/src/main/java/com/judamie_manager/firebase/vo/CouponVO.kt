package com.judamie_manager.firebase.vo

import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.util.CouponUsableType

class CouponVO {
    // 쿠폰 이름
    var couponName = ""
    // 쿠폰 할인율
    var couponDiscountRate = 1
    // 쿠폰 사용 기한
    var couponPeriod = 0L
    // 쿠폰 사용 가능 여부
    var couponState = 1
    // 시간
    var couponTimeStamp = 0L

    fun toCouponModel(couponDocumentID:String) : CouponModel {
        val couponModel = CouponModel()

        couponModel.couponDocumentID = couponDocumentID
        couponModel.couponName = couponName
        couponModel.couponDiscountRate = couponDiscountRate
        couponModel.couponPeriod = couponPeriod
        couponModel.couponTimeStamp = couponTimeStamp

        when(couponState){
            CouponUsableType.COUPON_USABLE.num -> couponModel.couponState = CouponUsableType.COUPON_USABLE
            CouponUsableType.COUPON_UNUSABLE.num -> couponModel.couponState = CouponUsableType.COUPON_UNUSABLE
        }

        return couponModel
    }
}