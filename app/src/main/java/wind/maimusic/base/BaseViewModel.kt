package wind.maimusic.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wind.maimusic.base.state.State
import wind.maimusic.net.ApiService
import wind.maimusic.net.RetrofitClient

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
}