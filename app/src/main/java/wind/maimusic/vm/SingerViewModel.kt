package wind.maimusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseViewModel
import wind.maimusic.base.state.State
import wind.maimusic.base.state.StateType
import wind.maimusic.model.singer.AllSingers
import wind.maimusic.model.singer.RecommendSingers
import wind.maimusic.model.singer.Singer
import wind.maimusic.model.singer.SingerSexClassify
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.nextInt

class SingerViewModel : BaseViewModel() {
    val singerData: MutableLiveData<MutableList<Any>> = MutableLiveData()
    private val mData = mutableListOf<Any>()

    //    fun findAllSingers() {
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                OnlineSongDatabase.getDatabase().singerDao().findAllSingers()
//            }
//            LogUtil.e("thread:${result.size}")
//            if (isNotNullOrEmpty(result)) {
//                allSingers.value =result.toMutableList()
//            }
//        }
//    }
    fun initSingersData() {
        loadStatus.value = State(StateType.LOADING_TOP)
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
                when (id) {
                    Constants.SINGER_JAY_CHOU -> {
                        val jayChou = singerDao.findSingerBySingerId(id)
                        jayChou.cover = Constants.jayChouImgs[nextInt(6)]
                        recommendSingers.add(jayChou)
                    }
                    Constants.SINGER_XUE -> {
                        val xue = singerDao.findSingerBySingerId(id)
                        xue.cover = Constants.xueImgs[nextInt(6)]
                        recommendSingers.add(xue)
                    }
                    Constants.SINGER_EASON -> {
                        val eaSon = singerDao.findSingerBySingerId(id)
                        eaSon.cover = Constants.eaSonImgs[nextInt(6)]
                        recommendSingers.add(eaSon)
                    }
                    Constants.SINGER_DENG_ZQ -> {
                        val dengZq = singerDao.findSingerBySingerId(id)
                        dengZq.cover = Constants.dengZqImgs[nextInt(6)]
                        recommendSingers.add(dengZq)
                    }
                    Constants.SINGER_REN_RAN -> {
                        val renRan = singerDao.findSingerBySingerId(id)
                        renRan.cover = Constants.renRanImgs[nextInt(5)]
                        recommendSingers.add(renRan)
                    }
                    Constants.SINGER_WAIT_JUN -> {
                        val waitJun = singerDao.findSingerBySingerId(id)
                        waitJun.cover = Constants.waitJunImgs[nextInt(4)]
                        recommendSingers.add(waitJun)
                    }
                }
            }
            val recommendSinger = RecommendSingers(recommendSingers)
            mData.add(recommendSinger)
            val sexClassify =
                SingerSexClassify(R.string.man.getStringRes(), R.string.woman.getStringRes())
            mData.add(sexClassify)
            val result = withContext(Dispatchers.IO) {
                singerDao.findAllSingers()
            }
            LogUtil.e("all Singers:${result.size}")
            if (isNotNullOrEmpty(result)) {
                val allSinger = AllSingers(result)
                mData.add(allSinger)
            }
            loadStatus.value = State(StateType.DISMISSING_TOP)
            singerData.value = mData
        }
    }
}