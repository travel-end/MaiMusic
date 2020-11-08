package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.Constants
import wind.maimusic.base.BaseViewModel
import wind.maimusic.model.singer.Singer
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.nextInt

class SingerViewModel:BaseViewModel() {
    val allSingers:MutableLiveData<List<Singer>> = MutableLiveData()
    val mRecommendSingers:MutableLiveData<List<Singer>> = MutableLiveData()

    fun findAllSingers() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                OnlineSongDatabase.getDatabase().singerDao().findAllSingers()
            }
            if (result.isNotEmpty()) {
                allSingers.value =result
            }
        }
    }
    fun initRecommendSingers() {
        val singerIds = intArrayOf(
            Constants.SINGER_JAY_CHOU,
            Constants.SINGER_XUE,
            Constants.SINGER_EASON,
            Constants.SINGER_DENG_ZQ,
            Constants.SINGER_REN_RAN,
            Constants.SINGER_WAIT_JUN
        )
        val singerDao = OnlineSongDatabase.getDatabase().singerDao()
        val recommendSingers = mutableListOf<Singer>()
        request {
            for (id in singerIds) {
                when(id) {
                    Constants.SINGER_JAY_CHOU->{
                        val jayChou =singerDao.findSingerBySingerId(id)
                        jayChou.cover = Constants.jayChouImgs[nextInt(6)]
                        recommendSingers.add(jayChou)
                    }
                    Constants.SINGER_XUE->{
                        val xue =singerDao.findSingerBySingerId(id)
                        xue.cover = Constants.xueImgs[nextInt(6)]
                        recommendSingers.add(xue)
                    }
                    Constants.SINGER_EASON->{
                        val eaSon =singerDao.findSingerBySingerId(id)
                        eaSon.cover = Constants.eaSonImgs[nextInt(6)]
                        recommendSingers.add(eaSon)
                    }
                    Constants.SINGER_DENG_ZQ->{
                        val dengZq =singerDao.findSingerBySingerId(id)
                        dengZq.cover = Constants.dengZqImgs[nextInt(6)]
                        recommendSingers.add(dengZq)
                    }
                    Constants.SINGER_REN_RAN->{
                        val renRan =singerDao.findSingerBySingerId(id)
                        renRan.cover = Constants.renRanImgs[nextInt(5)]
                        recommendSingers.add(renRan)
                    }
                    Constants.SINGER_WAIT_JUN->{
                        val waitJun =singerDao.findSingerBySingerId(id)
                        waitJun.cover = Constants.waitJunImgs[nextInt(4)]
                        recommendSingers.add(waitJun)
                    }
                }
            }
            mRecommendSingers.value = recommendSingers
        }
    }
}