package com.judamie_manager.firebase.model

import com.judamie_manager.firebase.vo.ManagerVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import com.judamie_manager.util.ManagerStateType
import com.judamie_manager.util.PickupStateType

class ManagerModel {

    // 관리자 id
    var managerId = ""
    // 로그인 아이디
    var managerLoginId = ""
    // 로그인 비밀번호
    var managerLoginPassword = ""
    // 자동 로그인
    var managerAutoLoginToken = ""
    // 관리자 상태
    var managerState = ManagerStateType.MANAGER_STATE_NORMAL
    // 시간
    var managerTimeStamp = 0L

    fun toManagerVO(): ManagerVO {
        val managerVO = ManagerVO()
        managerVO.managerLoginId = managerLoginId
        managerVO.managerLoginPassword = managerLoginPassword
        managerVO.managerAutoLoginToken = managerAutoLoginToken
        managerVO.managerState = managerState.num
        managerVO.managerTimeStamp = managerTimeStamp

        return managerVO
    }
}
