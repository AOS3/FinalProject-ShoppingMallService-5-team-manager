package com.judamie_manager.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.activity.UserActivity
import com.judamie_manager.databinding.FragmentLoginBinding
import com.judamie_manager.firebase.service.ManagerService
import com.judamie_manager.viewmodel.fragmentviewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var userActivity: UserActivity

    lateinit var managerId : String
    lateinit var managerPassword : String
    lateinit var managerData : Map<String, *>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        fragmentLoginBinding.loginViewModel = LoginViewModel(this@LoginFragment)
        fragmentLoginBinding.lifecycleOwner = this@LoginFragment

        userActivity = activity as UserActivity

        return fragmentLoginBinding.root
    }

    fun gettingData() {
        // 서버에서 관리자 데이터 가져오기
        CoroutineScope(Dispatchers.Main).launch {
            managerData = async(Dispatchers.IO) {
                ManagerService.gettingManagerData()
            }.await()

            // 가져온 데이터에서 ID와 비밀번호 추출
            val managerId = managerData["managerLoginId"].toString()
            val managerPassword = managerData["managerLoginPassword"].toString()

            proLogin(managerId, managerPassword)

        }
    }
    
    // 로그인 처리 메서드
    fun proLogin(managerId: String, managerPassword: String) {
        fragmentLoginBinding.apply {

//            // 서버에서 관리자 데이터 가져오기
//            CoroutineScope(Dispatchers.Main).launch{
//                managerData = async(Dispatchers.IO){
//                    ManagerService.gettingManagerData()
//                }.await()
//
//                // 가져온 데이터에서 ID와 비밀번호 추출
//                val managerId = managerData["managerLoginId"].toString()
//                val managerPassword = managerData["managerLoginPassword"].toString()
//
//                proLogin(managerId, managerPassword)
//
//            }
            textFieldUserLoginId.error = null
            textFieldUserLoginPw.error = null


            // 비밀번호 입력 여부 확인
            if (loginViewModel?.textFieldUserLoginPwEditTextText?.value?.isEmpty()!!) {
                textFieldUserLoginPw.error = "비밀번호를 입력해주세요."
                textFieldUserLoginPw.requestFocus()
                // userActivity.showSoftInput(textFieldUserLoginPw.editText!!)
                // 1초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(textFieldUserLoginPw, 2000)
            } else {
                textFieldUserLoginPw.error = null
            }

            // 아이디 입력 여부 확인
            if (loginViewModel?.textFieldUserLoginIdEditTextText?.value?.isEmpty()!!) {
                textFieldUserLoginId.error = "아이디를 입력해주세요."
                textFieldUserLoginId.requestFocus() // 아이디 필드에 포커스를 맞춘다.
                // userActivity.showSoftInput(textFieldUserLoginId.editText!!)
                // 1초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(textFieldUserLoginId, 2000)
            } else {
                textFieldUserLoginId.error = null

            }

            // 사용자가 입력한 아이디와 비밀번호
            val loginUserId = loginViewModel?.textFieldUserLoginIdEditTextText?.value!!
            val loginUserPw = loginViewModel?.textFieldUserLoginPwEditTextText?.value!!

            if (loginUserId.isNotEmpty() && loginUserPw.isNotEmpty()) {
                // 아이디, 비밀번호 같을 시 ServiceActivity로 이동
                if (managerId == loginUserId && managerPassword == loginUserPw) {

                    // 만약 자동 로그인이 체크돼 있을 경우
                    if(loginViewModel?.checkBoxUserLoginAutoChecked?.value!!){
                        CoroutineScope(Dispatchers.Main).launch{
                            async(Dispatchers.IO){
                                ManagerService.updateAutoLoginToken(userActivity, managerData["documentId"].toString())
                            }
                        }
                    }

                    val intent = Intent(userActivity, ServiceActivity::class.java)
                    startActivity(intent)
                    userActivity.finish()

                } else {
                    loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                    loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                    textFieldUserLoginId.error = "아이디 혹은 비밀번호가 잘못되었습니다."
                    textFieldUserLoginPw.error = "아이디 혹은 비밀번호가 잘못되었습니다."

                    // 1초 후에 에러 메시지 제거
//                    clearErrorAfterDelay(textFieldUserLoginId, 1000)
//                    clearErrorAfterDelay(textFieldUserLoginPw, 1000)
                }
            }
        }
    }

    // 에러 메시지 제거 메서드
    fun clearErrorAfterDelay(textField: TextInputLayout, delayMillis: Long) {
        Handler().postDelayed({
            textField.error = null
        }, delayMillis)
    }
}
