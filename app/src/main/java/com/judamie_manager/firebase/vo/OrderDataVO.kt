package com.judamie_manager.firebase.vo

import com.judamie_manager.firebase.model.OrderDataModel
import com.judamie_manager.firebase.model.OrderPackageModel
import com.judamie_manager.util.OrderPackageStateType
import com.judamie_manager.util.OrderStateType

class OrderDataVO {

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
    var orderState = 1

    fun toOrderDataModel(orderDocumentID:String) : OrderDataModel {
        val orderDataModel = OrderDataModel()

        orderDataModel.orderDocumentID = orderDocumentID
        orderDataModel.sellerDocumentID = sellerDocumentID
        orderDataModel.orderTime = orderTime
        orderDataModel.productDocumentID = productDocumentID
        orderDataModel.productPrice = productPrice
        orderDataModel.productDiscountRate = productDiscountRate
        orderDataModel.orderCount = orderCount
        orderDataModel.orderPriceAmount = orderPriceAmount
        orderDataModel.pickupLocDocumentID = pickupLocDocumentID
        orderDataModel.orderTimeStamp = orderTimeStamp

        when(orderState){
            OrderStateType.ORDER_STATE_PAYMENT_COMPLETE.num -> orderDataModel.orderState = OrderStateType.ORDER_STATE_PAYMENT_COMPLETE
            OrderStateType.ORDER_STATE_DELIVERY.num -> orderDataModel.orderState = OrderStateType.ORDER_STATE_DELIVERY
            OrderStateType.ORDER_STATE_PICKUP_COMPLETED.num -> orderDataModel.orderState = OrderStateType.ORDER_STATE_PICKUP_COMPLETED
            OrderStateType.ORDER_STATE_TRANSFER_COMPLETED.num -> orderDataModel.orderState = OrderStateType.ORDER_STATE_TRANSFER_COMPLETED

        }

        return orderDataModel
    }
}