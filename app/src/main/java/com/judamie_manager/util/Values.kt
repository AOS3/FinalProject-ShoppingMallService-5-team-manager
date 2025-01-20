package com.judamie_manager.util

// 서비스 프래그먼트들을 나타내는 값들
enum class ServiceFragmentName(var number:Int, var str:String){
    // 게시판 메인 화면
    MAIN_FRAGMENT(1, "MainFragment"),
    // 쿠폰 목록 화면
    COUPON_LIST_FRAGMENT(2, "CouponListFragment"),
    // 쿠폰 추가 화면
    ADD_COUPON_FRAGMENT(3, "AddCouponFragment"),
    // 거래 완료 내역 목록 화면
    COMPLETED_TRANSACTIONS_LIST_FRAGMENT(4,"CompletedTransactionsListFragment"),
    // 거래 완료 내역 상세 보기 화면
    SHOW_ONE_COMPLETED_TRANSACTION_DETAIL_FRAGMENT(5, "ShowOneCompletedTransactionDetailFragment"),
    // 거래 처리 중인 내역 목록 보기 화면
    TRANSACTIONS_LIST_FRAGMENT(6, "TransactionsListFragment"),
    // 거래 처리 중인 내역 상세 보기 화면
    PROCESSING_TRANSACTION_FRAGMENT(7, "ProcessingTransactionFragment"),
    // 픽업지 관리 화면
    SETTING_PICKUP_LOCATION(8, "SettingPickupLocationFragment"),
    // 픽업지 추가 화면
    ADD_PICKUP_LOCATION_FRAGMENT(9,"AddPickupLocationFragment"),
    // 다음 우편번호 api 실행 화면
    DAUM_API_FRAGMENT(10, "DaumApiFragment"),
    // 픽업지 지도 화면
    PICKUP_GOOGLE_MAP_FRAGMENT(11, "PickupGoogleMapFragment")
}

// 유저 프래그먼트를 나타내는 값
enum class UserFragmentName(var number:Int, var str:String){
    // 로그인 화면
    USER_LOGIN_FRAGMENT(1, "UserLoginFragment"),
}

// 쿠폰 사용 여부를 나타내는 값
enum class CouponUsableType(var num:Int, var str: String){
    // 사용 가능
    COUPON_USABLE(1, "사용 가능"),
    // 사용 불가능
    COUPON_UNUSABLE(2, "사용 불가능")
}

// 픽업지 상태를 나타내는 값
enum class PickupStateType(var num:Int, var str: String){
    // 기본
    PICKUP_STATE_NORMAL(1, "정상"),
    // 삭제
    PICKUP_STATE_DELETE(2, "삭제")
}