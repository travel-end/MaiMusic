package wind.maimusic.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wind.maimusic.base.state.State

/**
 * @By Journey 2020/10/25
 * @Description
 */
open class BaseViewModel:ViewModel() {
    val loadStatus by lazy {
        MutableLiveData<State>()
    }
}