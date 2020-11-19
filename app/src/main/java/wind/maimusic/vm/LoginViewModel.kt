package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import wind.maimusic.Constants
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.cuter.Cuter
import wind.maimusic.utils.CuterManager
import wind.maimusic.utils.StringUtil
import wind.maimusic.utils.nextInt

class LoginViewModel:BaseViewModel() {

    val loginData:MutableLiveData<Boolean> = MutableLiveData()

    val loginStatus:MutableLiveData<String> = MutableLiveData()

    fun seeLoginData(phone:String,password:String) {
        loginData.value = StringUtil.checkPhone(phone) && StringUtil.passwordCheck(password)
    }

    fun login(phone:String,password:String) {
//        val dao = MaiDatabase.getDatabase().cuterDao()
        val nickName = Constants.nickNames[nextInt(Constants.nickNames.size)]
        val cuter = Cuter(
            phone = phone,
            password = password,
            cuterCover = Constants.TEMP_AVATAR,
            nickName = nickName
        )
        request {
//            dao.deleteCuter()
//            val result = dao.addOneCuter(cuter)
//            if (result != 0L) {
//            }
            CuterManager.setCuter(cuter)
            loginStatus.value = nickName
        }
    }
}