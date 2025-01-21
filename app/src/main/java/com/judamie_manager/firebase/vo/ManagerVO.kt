package com.judamie_manager.firebase.vo

import com.judamie_manager.firebase.model.ManagerModel
import com.judamie_manager.util.ManagerStateType

class ManagerVO {

    // 로그인 아이디
    var managerLoginId = ""
    // 로그인 비밀번호
    var managerLoginPassword = ""
    // 자동 로그인
    var managerAutoLoginToken = ""
    // 관리자 상태
    var managerState = 1
    // 시간
    var managerTimeStamp = 0L

    fun toManagerModel(managerId:String) : ManagerModel {
        val managerModel = ManagerModel()

        managerModel.managerId = managerId
        managerModel.managerLoginId = managerLoginId
        managerModel.managerLoginPassword = managerLoginPassword
        managerModel.managerAutoLoginToken = managerAutoLoginToken
        managerModel.managerTimeStamp = managerTimeStamp

        when(managerState){
            ManagerStateType.MANAGER_STATE_NORMAL.num -> managerModel.managerState = ManagerStateType.MANAGER_STATE_NORMAL
            ManagerStateType.MANAGER_STATE_DELETE.num -> managerModel.managerState = ManagerStateType.MANAGER_STATE_DELETE
        }

        return managerModel
    }
}