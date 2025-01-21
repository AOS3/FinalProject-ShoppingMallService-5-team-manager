package com.judamie_manager.firebase.service

import android.content.Context
import androidx.core.content.edit
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.judamie_manager.firebase.model.ManagerModel
import com.judamie_manager.firebase.repository.ManagerRepository
import kotlinx.coroutines.tasks.await

class ManagerService {

    companion object {
        // 관리자 데이터를 가져오는 메서드
        suspend fun gettingManagerData() : Map<String, *> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ManagerData")

            // 데이터를 가져온다
            val result = collectionReference.get().await()

            // 첫 번째 문서 데이터만 반환
            val document = result.documents[0]
            val documentMap = document.data?.toMutableMap() ?: mutableMapOf()
            documentMap["documentId"] = document.id // 문서 ID 추가

            return documentMap
        }

        // 자동 로그인 토큰값을 갱신하는 메서드
        suspend fun updateAutoLoginToken(context: Context, managerId:String){
            // 새로운 토큰값을 발행한다.
            val newToken = "${managerId}${System.nanoTime()}"
            // SharedPreference에 저장한다.
            val pref = context.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
            pref.edit {
                putString("token", newToken)
            }
            // 서버에 저장한다.
            ManagerRepository.updateAutoLoginToken(managerId, newToken)
        }

        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectManagerDataByLoginToken(loginToken:String) : String? {
            val loginMap = ManagerRepository.selectManagerDataByLoginToken(loginToken)
            if(loginMap == null){
                return null
            } else {
                return "ddd"
            }
        }
    }
}