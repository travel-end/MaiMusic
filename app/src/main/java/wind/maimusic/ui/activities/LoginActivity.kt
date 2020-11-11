package wind.maimusic.ui.activities

import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleActivity
import wind.maimusic.vm.LoginViewModel

class LoginActivity:BaseLifeCycleActivity<LoginViewModel>() {
    override fun layoutResId()= R.layout.activity_login
}