package wind.maimusic.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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

    protected fun request(block: suspend () -> Unit) {
        viewModelScope.launch {
            runCatching {
                block()
            }.onSuccess {
            }.onFailure {
                handleException(e = it, state = State(StateType.SHOW_TOAST))
            }
        }
    }

    protected fun <T> requestAsync(block:suspend () -> T): Deferred<T> {
        return viewModelScope.async {
            block.invoke()
        }
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
