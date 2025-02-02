package com.judamie_manager.firebase.repository

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.judamie_manager.firebase.model.OrderDataModel
import com.judamie_manager.firebase.vo.OrderDataVO
import com.judamie_manager.util.OrderStateType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrderRepository {

    companion object {

//        // orderData 컬렉션에서 orderState가 4인 문서 가져오기
//        suspend fun getOrderDataWithState(state: Int): MutableList<OrderDataModel> {
//            val firestore = FirebaseFirestore.getInstance()
//            val orderDocuments = firestore.collection("OrderData")
//                .whereEqualTo("orderState", state)
//                .get()
//                .await()
//
//            val orderList = mutableListOf<OrderDataModel>()
//
//
//            // Firestore에서 가져온 문서가 비어있다면 바로 빈 리스트 반환
//            if (orderDocuments.isEmpty) {
//                return orderList // 아무것도 담지 않고 빈 리스트 반환
//            }
//
//            for (document in orderDocuments) {
//
//                val data = document.data
//
//                if (data != null) {
//                    val orderData = OrderDataModel()
//
//                    orderData.orderDocumentID = document.id
//
//
//                    // Firestore에서 "orderState" 값을 Long으로 가져오고, 이를 Int로 변환 후 OrderStateType으로 매핑
//                    val orderStateLong = data["orderState"] as? Number ?: 0L
//                    orderData.orderState = OrderStateType.fromNumber(orderStateLong.toInt())
//
//                    // userDocumentID (판매자 문서 ID)
//                    orderData.userDocumentID = data["userDocumentId"] as? String ?: ""
//
//                    // sellerDocumentID (판매자 문서 ID)
//                    orderData.sellerDocumentID = data["sellerDocumentId"] as? String ?: ""
//
//                    // 주문 시간 (orderTime)
//                    orderData.orderTime = ((data["orderTime"] as? Number) ?: 0L) as Long
//
//                    // productDocumentID (상품 문서 ID)
//                    orderData.productDocumentID = data["productDocumentId"] as? String ?: ""
//
//                    // 개당 가격 (productPrice)
//                    orderData.productPrice = (data["productPrice"] as? Number)?.toInt() ?: 1
//
//                    // 할인율 (productDiscountRate)
//                    orderData.productDiscountRate =
//                        (data["productDiscountRate"] as? Number)?.toInt() ?: 1
//
//                    // 상품 개수 (orderCount)
//                    orderData.orderCount = (data["orderCount"] as? Number)?.toInt() ?: 0
//
//                    // 총 가격 (orderPriceAmount)
//                    orderData.orderPriceAmount = (data["orderPriceAmount"] as? Number)?.toInt() ?: 1
//
//                    // 픽업지 주소 문서 ID (pickupLocDocumentID)
//                    orderData.pickupLocDocumentID = data["pickupLocDocumentId"] as? String ?: ""
//
//                    // 시간 (orderTimeStamp)
//                    orderData.orderTimeStamp = ((data["orderTimeStamp"] as? Number) ?: 0L) as Long
//
//
//
//
//                    orderList.add(orderData)
//                }
//            }
//            return orderList
//        }

//        // orderData 컬렉션에서 orderState가 4가 아닌 문서 가져오기
//        suspend fun getOrderDataWithoutState(state: Int): MutableList<OrderDataModel> {
//            val firestore = FirebaseFirestore.getInstance()
//            val orderDocuments = firestore.collection("OrderData")
//                .whereNotEqualTo("orderState", state)
//                .get()
//                .await()
//
//            val orderList = mutableListOf<OrderDataModel>()
//
//
//            // Firestore에서 가져온 문서가 비어있다면 바로 빈 리스트 반환
//            if (orderDocuments.isEmpty) {
//                return orderList // 아무것도 담지 않고 빈 리스트 반환
//            }
//
//            for (document in orderDocuments) {
//
//                val data = document.data
//
//                if (data != null) {
//                    val orderData = OrderDataModel()
//
//                    orderData.orderDocumentID = document.id
//
//
//                    // Firestore에서 "orderState" 값을 Long으로 가져오고, 이를 Int로 변환 후 OrderStateType으로 매핑
//                    val orderStateLong = data["orderState"] as? Number ?: 0L
//                    orderData.orderState = OrderStateType.fromNumber(orderStateLong.toInt())
//
//                    // userDocumentID (판매자 문서 ID)
//                    orderData.userDocumentID = data["userDocumentId"] as? String ?: ""
//
//                    // sellerDocumentID (판매자 문서 ID)
//                    orderData.sellerDocumentID = data["sellerDocumentId"] as? String ?: ""
//
//                    // 주문 시간 (orderTime)
//                    orderData.orderTime = ((data["orderTime"] as? Number) ?: 0L) as Long
//
//                    // productDocumentID (상품 문서 ID)
//                    orderData.productDocumentID = data["productDocumentId"] as? String ?: ""
//
//                    // 개당 가격 (productPrice)
//                    orderData.productPrice = (data["productPrice"] as? Number)?.toInt() ?: 1
//
//                    // 할인율 (productDiscountRate)
//                    orderData.productDiscountRate =
//                        (data["productDiscountRate"] as? Number)?.toInt() ?: 1
//
//                    // 상품 개수 (orderCount)
//                    orderData.orderCount = (data["orderCount"] as? Number)?.toInt() ?: 0
//
//                    // 총 가격 (orderPriceAmount)
//                    orderData.orderPriceAmount = (data["orderPriceAmount"] as? Number)?.toInt() ?: 1
//
//                    // 픽업지 주소 문서 ID (pickupLocDocumentID)
//                    orderData.pickupLocDocumentID = data["pickupLocDocumentId"] as? String ?: ""
//
//                    // 시간 (orderTimeStamp)
//                    val orderTime = data["orderTime"] as? Long
//                    Log.d("OrderData", "orderTime: $orderTime")
//                    orderData.orderTime = orderTime ?: 0L
//
//
//                    orderList.add(orderData)
//                }
//            }
//            return orderList
//        }

        suspend fun getOrderDataWithoutState(state: Int): MutableList<OrderDataModel> {
            val firestore = FirebaseFirestore.getInstance()

            // orderState가 특정 값과 다른 문서만 가져오도록 쿼리 최적화
            val orderDocuments = firestore.collection("OrderData")
                .whereNotEqualTo("orderState", state)
                .get()
                .await()

            val orderList = mutableListOf<OrderDataModel>()

            // Firestore에서 가져온 문서가 비어있다면 바로 빈 리스트 반환
            if (orderDocuments.isEmpty) {
                return orderList // 아무것도 담지 않고 빈 리스트 반환
            }

            // 비동기적으로 데이터를 처리하여 성능 최적화
            orderDocuments.forEach { document ->
                val data = document.data

                // 데이터가 null이 아니면 처리
                data?.let {
                    val orderData = OrderDataModel().apply {
                        orderDocumentID = document.id
                        orderState = OrderStateType.fromNumber((it["orderState"] as? Number)?.toInt() ?: 0)
                        userDocumentID = it["userDocumentId"] as? String ?: ""
                        sellerDocumentID = it["sellerDocumentId"] as? String ?: ""
                        orderTime = (it["orderTime"] as? Number)?.toLong() ?: 0L
                        productDocumentID = it["productDocumentId"] as? String ?: ""
                        productPrice = (it["productPrice"] as? Number)?.toInt() ?: 1
                        productDiscountRate = (it["productDiscountRate"] as? Number)?.toInt() ?: 1
                        orderCount = (it["orderCount"] as? Number)?.toInt() ?: 0
                        orderPriceAmount = (it["orderPriceAmount"] as? Number)?.toInt() ?: 1
                        pickupLocDocumentID = it["pickupLocDocumentId"] as? String ?: ""
                    }

                    orderList.add(orderData)
                }
            }

            return orderList
        }

        suspend fun getOrderDataWithState(state: Int): MutableList<OrderDataModel> {
            val firestore = FirebaseFirestore.getInstance()

            // orderState가 특정 값인 문서만 가져오도록 쿼리 최적화
            val orderDocuments = firestore.collection("OrderData")
                .whereEqualTo("orderState", state)
                .get()
                .await()

            val orderList = mutableListOf<OrderDataModel>()

            // Firestore에서 가져온 문서가 비어있다면 바로 빈 리스트 반환
            if (orderDocuments.isEmpty) {
                return orderList // 아무것도 담지 않고 빈 리스트 반환
            }

            // 비동기적으로 데이터를 처리하여 성능 최적화
            withContext(Dispatchers.Default) {
                orderDocuments.forEach { document ->
                    val data = document.data

                    // 데이터가 null이 아니면 처리
                    data?.let {
                        val orderData = OrderDataModel().apply {
                            orderDocumentID = document.id
                            orderState = OrderStateType.fromNumber(
                                (it["orderState"] as? Number)?.toInt() ?: 0
                            )
                            userDocumentID = it["userDocumentId"] as? String ?: ""
                            sellerDocumentID = it["sellerDocumentId"] as? String ?: ""
                            orderTime = (it["orderTime"] as? Number)?.toLong() ?: 0L
                            productDocumentID = it["productDocumentId"] as? String ?: ""
                            productPrice = (it["productPrice"] as? Number)?.toInt() ?: 1
                            productDiscountRate =
                                (it["productDiscountRate"] as? Number)?.toInt() ?: 1
                            orderCount = (it["orderCount"] as? Number)?.toInt() ?: 0
                            orderPriceAmount = (it["orderPriceAmount"] as? Number)?.toInt() ?: 1
                            pickupLocDocumentID = it["pickupLocDocumentId"] as? String ?: ""
                            orderTimeStamp = (it["orderTimeStamp"] as? Number)?.toLong() ?: 0L
                        }

                        orderList.add(orderData)
                    }
                }
            }

            return orderList
        }



        // sellerDocumentID를 기반으로 sellerName 가져오기
        suspend fun getSellerNamesByIds(sellerIds: List<String>): MutableList<String> {
            val firestore = FirebaseFirestore.getInstance()
            val sellerNames = mutableListOf<String>()

            // 요청 시작 시간 기록
            val startTime = System.currentTimeMillis()
            Log.d("FirestoreSpeedTest", "Firebase 요청 시작: $startTime ms")


            // Firebase 요청을 병렬로 실행, 비어있는 sellerId는 "Unknown" 추가
            val sellerRequests = sellerIds.map { sellerId ->
                if (sellerId.isNotEmpty()) {
                    firestore.collection("SellerData").document(sellerId).get()
                } else {
                    null // 비어있는 경우 null을 반환하여 이후 처리에서 제외
                }
            }

            // 비어있는 sellerId에 대한 "Unknown" 추가
            sellerIds.forEach { sellerId ->
                if (sellerId.isEmpty()) {
                    sellerNames.add("Unknown")
                }
            }

            // Firebase 요청 리스트에서 null 제거
            val validRequests = sellerRequests.filterNotNull()

            if (validRequests.isEmpty()) {
                return sellerNames // 모든 sellerId가 비어 있으면 바로 반환
            }

            try {
                // Firebase 요청 병렬 처리
                val results = Tasks.whenAllSuccess<DocumentSnapshot>(validRequests).await()

                // 결과 처리
                results.forEach { document ->
                    if (document.exists()) {
                        val sellerName = document.getString("storeName") ?: "Unknown"
                        sellerNames.add(sellerName.takeIf { it.isNotEmpty() } ?: "Unknown")
                    } else {
                        sellerNames.add("Unknown")
                    }
                }

                // 요청 종료 시간 기록
                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime
                Log.d("FirestoreSpeedTest", "Firebase 요청 완료: $endTime ms, 실행 시간: $elapsedTime ms")

            } catch (e: Exception) {
                // 예외 처리
                e.printStackTrace()
            }


//            sellerIds.forEach { sellerId ->
//                // sellerId가 비어 있지 않으면 Firebase에서 문서 가져오기
//                if (sellerId.isNotEmpty()) {
//                    val sellerDocument =
//                        firestore.collection("SellerData").document(sellerId).get().await()
//
//                    // 문서가 존재하면 sellerName을 가져옴
//                    if (sellerDocument.exists()) {
//                        val sellerName = sellerDocument.getString("storeName") ?: "Unknown"
//                        // 값이 있을 경우에만 리스트에 추가
//                        if (!sellerName.isNullOrEmpty()) {
//                            sellerNames.add(sellerName)
//                        } else {
//                            sellerNames.add("Unknown")
//                        }
//                    } else {
//                        // 문서가 없을 경우 "Unknown"
//                        sellerNames.add("Unknown")
//                    }
//                } else {
//                    // sellerId가 비어 있으면 "Unknown"
//                    sellerNames.add("Unknown")
//                }
//
//                // 요청 종료 시간 기록
//                val endTime = System.currentTimeMillis()
//                val elapsedTime = endTime - startTime
//                Log.d("FirestoreSpeedTest", "Firebase 요청 완료: $endTime ms, 실행 시간: $elapsedTime ms")
//            }

            return sellerNames
        }

        // userDocumentID를 기반으로 userName 가져오기
        suspend fun getUserNamesByIds(userIds: List<String>): MutableList<String> {
            val firestore = FirebaseFirestore.getInstance()
            val userNames = mutableListOf<String>()

            // 요청 시작 시간 기록
            val startTime = System.currentTimeMillis()
            Log.d("FirestoreSpeedTest", "Firebase 요청 시작: $startTime ms")

            // Firebase 요청을 병렬로 실행 (비어있는 userId는 "Unknown" 추가)
            val userRequests = userIds.map { userId ->
                if (userId.isNotEmpty()) {
                    firestore.collection("UserData").document(userId).get()
                } else {
                    null
                }
            }

            // 비어있는 userId에 대한 "Unknown" 추가
            userIds.forEach { userId ->
                if (userId.isEmpty()) {
                    userNames.add("Unknown")
                }
            }

            // Firebase 요청 리스트에서 null 제거
            val validRequests = userRequests.filterNotNull()

            if (validRequests.isEmpty()) {
                return userNames // 모든 userId가 비어 있으면 바로 반환
            }

            try {
                // Firebase 요청 병렬 처리
                val results = Tasks.whenAllSuccess<DocumentSnapshot>(validRequests).await()

                // 결과 처리
                results.forEach { document ->
                    if (document.exists()) {
                        val userName = document.getString("userName") ?: "Unknown"
                        userNames.add(userName.takeIf { it.isNotEmpty() } ?: "Unknown")
                    } else {
                        userNames.add("Unknown")
                    }
                }

                // 요청 종료 시간 기록 (한 번만 실행)
                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime
                Log.d("FirestoreSpeedTest", "Firebase 요청 완료: $endTime ms, 실행 시간: $elapsedTime ms")

            } catch (e: Exception) {
                Log.e("FirestoreSpeedTest", "Firebase 요청 중 오류 발생: ${e.message}")
            }

//            userIds.forEach { userId ->
//                // userId가 비어 있지 않으면 Firebase에서 문서 가져오기
//                if (userId.isNotEmpty()) {
//                    val userDocument = firestore.collection("UserData").document(userId).get().await()
//                    val userName = userDocument.getString("userName") ?: "Unknown"
//
//                    // 값이 있을 경우에만 리스트에 추가
//                    if (!userName.isNullOrEmpty()) {
//                        userNames.add(userName)
//                    } else {
//                        userNames.add("Unknown")
//                    }
//                } else {
//                    // userId가 비어 있으면 "Unknown"
//                    userNames.add("Unknown")
//                }
//            }
            return userNames
        }

        // pickupLocDocumentID 기반으로 pickupLocName 가져오기
        suspend fun getPickupNamesByIds(pickupIds: List<String>): MutableList<String> {
            val firestore = FirebaseFirestore.getInstance()
            val pickupNames = mutableListOf<String>()

            pickupIds.forEach { pickupId ->
                // pickupId가 비어 있지 않으면 Firebase에서 문서 가져오기
                if (pickupId.isNotEmpty()) {
                    val pickupDocument = firestore.collection("PickupLocationData").document(pickupId).get().await()
                    val pickupName = pickupDocument.getString("pickupLocName") ?: "Unknown"

                    // 값이 있을 경우에만 리스트에 추가
                    if (!pickupName.isNullOrEmpty()) {
                        pickupNames.add(pickupName)
                    } else {
                        pickupNames.add("Unknown")
                    }
                } else {
                    // pickupId가 비어 있으면 "Unknown"
                    pickupNames.add("Unknown")
                }
            }
            return pickupNames
        }

        // productDocumentID 기반으로 productName 가져오기
        suspend fun getProductNamesByIds(productIds: List<String>): MutableList<String> {
            val firestore = FirebaseFirestore.getInstance()
            val productNames = mutableListOf<String>()

            // 요청 시작 시간 기록
            val startTime = System.currentTimeMillis()
            Log.d("FirestoreSpeedTest", "Firebase 요청 시작: $startTime ms")

            // Firebase 요청을 병렬로 실행 (비어있는 productId는 "Unknown" 추가)
            val productRequests = productIds.map { productId ->
                if (productId.isNotEmpty()) {
                    firestore.collection("ProductData").document(productId).get()
                } else {
                    null
                }
            }

            // 비어있는 productId에 대한 "Unknown" 추가
            productIds.forEach { productId ->
                if (productId.isEmpty()) {
                    productNames.add("Unknown")
                }
            }

            // Firebase 요청 리스트에서 null 제거
            val validRequests = productRequests.filterNotNull()

            if (validRequests.isEmpty()) {
                return productNames // 모든 productId가 비어 있으면 바로 반환
            }

            try {
                // Firebase 요청 병렬 처리
                val results = Tasks.whenAllSuccess<DocumentSnapshot>(validRequests).await()

                // 결과 처리
                results.forEach { document ->
                    if (document.exists()) {
                        val productName = document.getString("productName") ?: "Unknown"
                        productNames.add(productName.takeIf { it.isNotEmpty() } ?: "Unknown")
                    } else {
                        productNames.add("Unknown")
                    }
                }

                // 요청 종료 시간 기록 (한 번만 실행)
                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime
                Log.d("FirestoreSpeedTest", "Firebase 요청 완료: $endTime ms, 실행 시간: $elapsedTime ms")

            } catch (e: Exception) {
                Log.e("FirestoreSpeedTest", "Firebase 요청 중 오류 발생: ${e.message}")
            }

//            productIds.forEach { productId ->
//                // productId가 비어 있지 않으면 Firebase에서 문서 가져오기
//                if (productId.isNotEmpty()) {
//                    val pickupDocument = firestore.collection("ProductData").document(productId).get().await()
//                    val productName = pickupDocument.getString("productName") ?: "Unknown"
//
//                    // 값이 있을 경우에만 리스트에 추가
//                    if (!productName.isNullOrEmpty()) {
//                        productNames.add(productName)
//                    } else {
//                        productNames.add("Unknown")
//                    }
//                } else {
//                    // productId가 비어 있으면 "Unknown"
//                    productNames.add("Unknown")
//                }
//            }
            return productNames
        }

        // productDocumentID 기반으로 해당 상품 카테고리 가져오기
        suspend fun getProductCategoryByIds(productIds: List<String>): MutableList<String> {
            val firestore = FirebaseFirestore.getInstance()
            val productCategories = mutableListOf<String>()

            // 요청 시작 시간 기록
            val startTime = System.currentTimeMillis()
            Log.d("FirestoreSpeedTest", "Firebase 요청 시작: $startTime ms")

            // Firebase 요청을 병렬로 실행 (비어있는 productId는 "Unknown" 추가)
            val productRequests = productIds.map { productId ->
                if (productId.isNotEmpty()) {
                    firestore.collection("ProductData").document(productId).get()
                } else {
                    null
                }
            }

            // 비어있는 productId에 대한 "Unknown" 추가
            productIds.forEach { productId ->
                if (productId.isEmpty()) {
                    productCategories.add("Unknown")
                }
            }

            // Firebase 요청 리스트에서 null 제거
            val validRequests = productRequests.filterNotNull()

            if (validRequests.isEmpty()) {
                return productCategories // 모든 productId가 비어 있으면 바로 반환
            }

            try {
                // Firebase 요청 병렬 처리
                val results = Tasks.whenAllSuccess<DocumentSnapshot>(validRequests).await()

                // 결과 처리
                results.forEach { document ->
                    if (document.exists()) {
                        val productCategory = document.getString("productCategory") ?: "Unknown"
                        productCategories.add(productCategory.takeIf { it.isNotEmpty() } ?: "Unknown")
                    } else {
                        productCategories.add("Unknown")
                    }
                }

                // 요청 종료 시간 기록 (한 번만 실행)
                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime
                Log.d("FirestoreSpeedTest", "Firebase 요청 완료: $endTime ms, 실행 시간: $elapsedTime ms")

            } catch (e: Exception) {
                Log.e("FirestoreSpeedTest", "Firebase 요청 중 오류 발생: ${e.message}")
            }
//            productIds.forEach { productId ->
//                // productId가 비어 있지 않으면 Firebase에서 문서 가져오기
//                if (productId.isNotEmpty()) {
//                    val pickupDocument = firestore.collection("ProductData").document(productId).get().await()
//                    val productName = pickupDocument.getString("productCategory") ?: "Unknown"
//
//                    // 값이 있을 경우에만 리스트에 추가
//                    if (!productName.isNullOrEmpty()) {
//                        productNames.add(productName)
//                    } else {
//                        productNames.add("Unknown")
//                    }
//                } else {
//                    // productId가 비어 있으면 "Unknown"
//                    productNames.add("Unknown")
//                }
//            }
            return productCategories
        }

        // 입금 완료 상태로 업데이트 하기
        fun updateOrderState(orderId : String) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("OrderData").document(orderId).update("orderState", 4)
        }

    }
}
