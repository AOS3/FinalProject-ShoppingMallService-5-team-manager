package com.judamie_manager.firebase.vo

import com.judamie_manager.firebase.model.OrderPackageModel
import com.judamie_manager.util.OrderPackageStateType

class OrderPackageVO {

    // 주문목록 리스트
    var orderDataList = mutableListOf<String>()
    // 주문자의 문서 id
    var orderOwner = ""
    // 상태값
    var orderPackageState = 1
    // 시간
    var orderDataTimeStamp = 0L
    // 거래 시간 (입금할떄의)
    var transactionTime = ""
    // 픽업 상태
    var pickupState = false
    // 입금 상태
    var depositState = false

    fun toOrderPackageModel(orderPackageDocumentID:String) : OrderPackageModel {
        val orderPackageModel = OrderPackageModel()

        orderPackageModel.orderPackageDocumentID = orderPackageDocumentID
        orderPackageModel.orderOwner = orderOwner
        orderPackageModel.orderDataTimeStamp = orderDataTimeStamp
        orderPackageModel.transactionTime = transactionTime
        orderPackageModel.pickupState = pickupState
        orderPackageModel.depositState = depositState

        when(orderPackageState){
            OrderPackageStateType.ORDER_PACKAGE_STATE_NORMAL.num -> orderPackageModel.orderPackageState = OrderPackageStateType.ORDER_PACKAGE_STATE_NORMAL
            OrderPackageStateType.ORDER_PACKAGE_STATE_DELETE.num -> orderPackageModel.orderPackageState = OrderPackageStateType.ORDER_PACKAGE_STATE_DELETE
        }

        return orderPackageModel
    }
}