package com.judamie_manager.viewmodel.fragmentviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_manager.ui.fragment.LoginFragment

class LoginViewModel (val loginFragment: LoginFragment) : ViewModel() {

    // textFieldUserLoginId - EditText - text
    val textFieldUserLoginIdEditTextText = MutableLiveData("")
    // textFieldUserLoginPw - EditText - text
    val textFieldUserLoginPwEditTextText = MutableLiveData("")
    // checkBoxUserLoginAuto - checked
    val checkBoxUserLoginAutoChecked = MutableLiveData(false)

    // buttonUserLoginJoin - onClick
    fun buttonUserLoginJoinOnClick(){
        // loginFragment.proLogin(managerId, managerPassword)
        loginFragment.gettingData()
    }
}