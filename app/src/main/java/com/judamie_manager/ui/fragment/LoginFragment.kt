package com.judamie_manager.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.activity.UserActivity
import com.judamie_manager.databinding.FragmentLoginBinding
import com.judamie_manager.viewmodel.fragmentviewmodel.LoginViewModel

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var userActivity: UserActivity

    // 임시 아이디, 비밀번호
    val aaaId = "1"
    val aaaPassword = "2"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        fragmentLoginBinding.loginViewModel = LoginViewModel(this@LoginFragment)
        fragmentLoginBinding.lifecycleOwner = this@LoginFragment

        userActivity = activity as UserActivity

        return fragmentLoginBinding.root
    }

    // 로그인 처리 메서드
    fun proLogin(){
        fragmentLoginBinding.apply {

            // 아이디 입력 여부 확인
            if (loginViewModel?.textFieldUserLoginIdEditTextText?.value?.isEmpty()!!) {
                textFieldUserLoginId.error = "아이디를 입력해주세요."
            } else {
                textFieldUserLoginId.error = null
            }

            // 비밀번호 입력 여부 확인
            if (loginViewModel?.textFieldUserLoginPwEditTextText?.value?.isEmpty()!!) {
                textFieldUserLoginPw.error = "비밀번호를 입력해주세요."
            } else {
                textFieldUserLoginPw.error = null
            }

            // 사용자가 입력한 아이디와 비밀번호
            val loginUserId = loginViewModel?.textFieldUserLoginIdEditTextText?.value!!
            val loginUserPw = loginViewModel?.textFieldUserLoginPwEditTextText?.value!!

            if (loginUserId.isNotEmpty() && loginUserPw.isNotEmpty()) {
                // 아이디, 비밀번호 같을 시 ServiceActivity로 이동
                if (aaaId == loginUserId && aaaPassword == loginUserPw) {
                    val intent = Intent(userActivity, ServiceActivity::class.java)
                    startActivity(intent)
                    userActivity.finish()
                } else {
                    loginViewModel?.textFieldUserLoginIdEditTextText?.value = ""
                    loginViewModel?.textFieldUserLoginPwEditTextText?.value = ""
                    textFieldUserLoginId.error = "아이디 혹은 비밀번호가 잘못되었습니다."
                    textFieldUserLoginPw.error = "아이디 혹은 비밀번호가 잘못되었습니다."
                }
            }
        }
    }
}
