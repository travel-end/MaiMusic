package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.Cuter
import wind.maimusic.room.database.MaiDatabase
import wind.maimusic.utils.StringUtil

class LoginViewModel:BaseViewModel() {

    val loginData:MutableLiveData<Boolean> = MutableLiveData()

    val loginStatus:MutableLiveData<Boolean> = MutableLiveData()

    fun seeLoginData(phone:String,password:String) {
        loginData.value = StringUtil.checkPhone(phone) && StringUtil.passwordCheck(password)
    }

    fun login(phone:String,password:String) {
        val dao = MaiDatabase.getDatabase().cuterDao()
        val cuter = Cuter(phone = phone,password = password)
        request {
            dao.deleteCuter()
            val result = dao.addOneCuter(cuter)
            if (result != 0L) {
                loginStatus.value = true
            }
        }
    }
}