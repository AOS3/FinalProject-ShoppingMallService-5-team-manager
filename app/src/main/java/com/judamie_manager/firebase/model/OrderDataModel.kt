package com.judamie_manager.firebase.model

import com.judamie_manager.firebase.vo.OrderDataVO
import com.judamie_manager.firebase.vo.OrderPackageVO
import com.judamie_manager.util.OrderStateType

class OrderDataModel {

    // 주문 문서 id
    var orderDocumentID = ""
    // 구매자 문서 id
    var userDocumentID = ""
    // 판매자 문서 id
    var sellerDocumentID = ""
    // 주문 시간
    var orderTime = 0L
    // 상품 문서 id
    var productDocumentID = ""
    // 개당 가격
    var productPrice = 1
    // 할인율
    var productDiscountRate = 1
    // 상품 개수
    var orderCount = 1
    // 총 가격
    var orderPriceAmount = 1
    // 픽업지 주소 문서 id
    var pickupLocDocumentID = ""
    // 시간
    var orderTimeStamp = 0L
    // 상태
    var orderState = OrderStateType.ORDER_STATE_PAYMENT_COMPLETE

    fun toOrderDataVO(): OrderDataVO {
        val orderDataVO = OrderDataVO()
        orderDataVO.userDocumentID = userDocumentID
        orderDataVO.sellerDocumentID = sellerDocumentID
        orderDataVO.orderTime = orderTime
        orderDataVO.orderState = orderState.num.toInt()
        orderDataVO.productDocumentID = productDocumentID
        orderDataVO.productPrice = productPrice
        orderDataVO.productDiscountRate = productDiscountRate
        orderDataVO.orderCount = orderCount
        orderDataVO.orderPriceAmount = orderPriceAmount
        orderDataVO.pickupLocDocumentID = pickupLocDocumentID
        orderDataVO.orderTimeStamp = orderTimeStamp

        return orderDataVO
    }


}