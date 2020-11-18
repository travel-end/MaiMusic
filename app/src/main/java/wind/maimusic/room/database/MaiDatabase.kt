package wind.maimusic.room.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import wind.maimusic.MaiApp
import wind.maimusic.model.*
import wind.maimusic.model.download.DownloadSong
import wind.maimusic.model.firstmeet.FirstMeetSong
import wind.maimusic.model.searchhot.HistoryTag
import wind.maimusic.model.searchhot.HotSearchSong
import wind.maimusic.model.searchhot.RecommendSearch
import wind.maimusic.room.*

/**
 * @By Journey 2020/10/28
 * @Description 关于我的音乐数据库（包括我听过的、本地音乐、下载音乐...）
 */
@Database(
    entities = [
        FirstMeetSong::class,
        LocalSong::class,
        HistorySong::class,
        DownloadSong::class,
        LoveSong::class,
        OnlineSong::class,
        RecommendSearch::class,
        HotSearchSong::class,
        HistoryTag::class,
        Cuter::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MaiDatabase : RoomDatabase() {
    abstract fun localSongDao(): LocalSongDao
    abstract fun historySongDao(): HistorySongDao
    abstract fun downloadSongDao(): DownloadSongDao
    abstract fun loveSongDao(): LoveSongDao
    abstract fun searchSongDao():SearchSongDao
    abstract fun cuterDao():CuterDao

    companion object {
        @Volatile
        private var INSTANCE: MaiDatabase? = null
        fun getDatabase(): MaiDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    MaiApp.getInstance().applicationContext,
                    MaiDatabase::class.java,
                    "database_about_me_song"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}