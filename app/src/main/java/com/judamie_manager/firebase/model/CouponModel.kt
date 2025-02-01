package com.judamie_manager.firebase.model

import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.util.CouponUsableType

class CouponModel {
    // 쿠폰 id
    var couponDocumentID = ""
    // 쿠폰 이름
    var couponName = ""
    // 쿠폰 할인율
    var couponDiscountRate = 1
    // 쿠폰 사용 기한
    var couponPeriod = 0L
    // 쿠폰 사용 가능 여부
    var couponState = CouponUsableType.COUPON_USABLE
    // 시간
    var couponTimeStamp = 0L

    fun toCouponVO():CouponVO{
        val couponVO = CouponVO()
        couponVO.couponName = couponName
        couponVO.couponDiscountRate = couponDiscountRate
        couponVO.couponPeriod = couponPeriod
        couponVO.couponState = couponState.num
        couponVO.couponTimeStamp = couponTimeStamp

        return couponVO
    }
}


