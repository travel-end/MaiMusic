package wind.maimusic.ui.activities

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_login.*
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleActivity
import wind.maimusic.utils.Bus
import wind.maimusic.utils.getColorRes
import wind.maimusic.vm.LoginViewModel
import wind.widget.utils.fastClickListener

class LoginActivity:BaseLifeCycleActivity<LoginViewModel>() {
    private var phone:String? = null
    private var password:String? = null
    override fun layoutResId()= R.layout.activity_login

    override fun initStatusBar() {
        ImmersionBar.with(this).transparentBar().init()
    }
    override fun initView() {
        super.initView()
        login_iv_back.fastClickListener {
            finish()
        }
        login_btn.fastClickListener {
            phone = login_et_phone.text.toString()
            password = login_et_password.text.toString()
            mViewModel.login(phone!!,password!!)
        }
        login_et_phone.doAfterTextChanged {
            phone = it.toString()
        }
        login_et_password.doAfterTextChanged {
            password = it.toString()
            mViewModel.seeLoginData(phone?:"",password?:"")
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.loginData.observe(this,Observer{
            if (it) {
                login_btn.isEnabled =true
                login_btn.setTextColor(R.color.white.getColorRes())
            } else {
                login_btn.isEnabled =false
                login_btn.setTextColor(R.color.white_50p.getColorRes())
            }
        })
        mViewModel.loginStatus.observe(this,Observer{
            Bus.post(Constants.LOGIN_SUCCESS,it)
            finish()
        })
    }
}