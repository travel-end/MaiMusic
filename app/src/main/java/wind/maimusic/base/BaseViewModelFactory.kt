package wind.maimusic.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @By Journey 2020/11/20
 * @Description
 */
class BaseViewModelFactory :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }
}