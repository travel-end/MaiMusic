package wind.maimusic.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import wind.maimusic.R
import wind.maimusic.base.state.State
import wind.maimusic.base.state.StateType
import wind.maimusic.net.ApiService
import wind.maimusic.net.RetrofitClient
import wind.maimusic.utils.getStringRes
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * @By Journey 2020/10/25
 * @Description
 */
open class BaseViewModel:ViewModel() {
    val loadStatus by lazy {
        MutableLiveData<State>()
    }
    protected val apiService: ApiService by lazy {
        RetrofitClient.instance.apiService
    }
    protected val singerApiService: ApiService by lazy {
        RetrofitClient.instance.singerApiService
    }
    protected val songUrlApiService: ApiService by lazy {
        RetrofitClient.instance.songUrlApiService
    }
    protected fun handleException(e: Throwable, state: State) {
        when (e) {
            is HttpException -> {
                loadStatus.value = state
            }
            is ConnectException -> {
                loadStatus.value = state
            }
            is ConnectTimeoutException -> {
                loadStatus.value = state
            }
            is UnknownHostException -> {
                loadStatus.value = state
            }
            is JsonParseException -> {
                loadStatus.value = state
            }
            is NoClassDefFoundError -> {
                loadStatus.value = state
            }
            else->{
                loadStatus.value = State(StateType.SHOW_TOAST,msg = R.string.unknow_error.getStringRes())
            }
        }
    }
}