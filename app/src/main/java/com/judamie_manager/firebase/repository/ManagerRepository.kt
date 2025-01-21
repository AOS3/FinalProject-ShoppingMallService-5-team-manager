package com.judamie_manager.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.firebase.model.ManagerModel
import com.judamie_manager.firebase.vo.ManagerVO
import kotlinx.coroutines.tasks.await

class ManagerRepository {

    companion object {
        // 자동로그인 토큰값을 갱신하는 메서드
        suspend fun updateAutoLoginToken(managerId:String, newToken:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ManagerData")
            val documentReference = collectionReference.document(managerId)
            val tokenMap = mapOf(
                "managerAutoLoginToken" to newToken
            )
            documentReference.update(tokenMap).await()
        }

        // 자동 로그인 토큰 값으로 관리자 정보를 가져오는 메서드
        suspend fun selectManagerDataByLoginToken(loginToken:String) : Map<String, *>?{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ManagerData")
            val result = collectionReference.whereEqualTo("managerAutoLoginToken", loginToken).get().await()
            // 문서가 없거나 첫 번째 문서가 없을 경우 null 반환
            if (result.isEmpty) return null

            // 문서 가져오기
            val document = result.documents[0]
            val documentData = document.data?.toMutableMap() ?: mutableMapOf()

            // 토큰 값 비교
            return if (documentData["managerAutoLoginToken"] == loginToken) {
                documentData["documentId"] = document.id // 문서 ID 추가
                documentData // 반환
            } else {
                null // 토큰이 일치하지 않으면 null 반환
            }
        }
    }
}