package com.judamie_manager.firebase.model

import com.judamie_manager.firebase.vo.OrderPackageVO
import com.judamie_manager.util.OrderPackageStateType

class OrderPackageModel {

    // 주문 패키지 문서 id
    var orderPackageDocumentID = ""
    // 주문 목록 리스트
    var orderDataList = mutableListOf<String>()
    // 주문자의 문서 id
    var orderOwner = ""
    // 상태값
    var orderPackageState = OrderPackageStateType.ORDER_PACKAGE_STATE_NORMAL
    // 시간
    var orderDataTimeStamp = 0L
    // 거래 시간 (입금할떄의)
    var transactionTime = ""
    // 픽업 상태
    var pickupState = false
    // 입금 상태
    var depositState = false

    fun toOrderPackageVO(): OrderPackageVO {
        val orderPackageVO = OrderPackageVO()
        orderPackageVO.orderDataList = orderDataList
        orderPackageVO.orderOwner = orderOwner
        orderPackageVO.orderDataTimeStamp = orderDataTimeStamp
        orderPackageVO.orderPackageState = orderPackageState.num
        orderPackageVO.transactionTime = transactionTime
        orderPackageVO.pickupState = pickupState
        orderPackageVO.depositState = depositState

        return orderPackageVO
    }
}