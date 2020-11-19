package wind.maimusic.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @By Journey 2020/11/19
 * @Description Activity与Fragment之间共享数据  Fragment与Fragment之间共享数据
 */
class ShareViewModel : ViewModel() {
    private val selectImage: MutableLiveData<String> = MutableLiveData()

    fun setSelectImage(image: String) {
        selectImage.value = image
//        selectImage.postValue(image)
    }

    fun getSelectImage(): LiveData<String> {
        return selectImage
    }

}