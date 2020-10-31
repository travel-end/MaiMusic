package wind.maimusic.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import wind.maimusic.MaiApp
import wind.maimusic.model.*
import wind.maimusic.model.download.DownloadSong
import wind.maimusic.model.firstmeet.FirstMeetSong

/**
 * @By Journey 2020/10/28
 * @Description 关于我的音乐数据库（包括我听过的、本地音乐、下载音乐...）
 */
@Database(
    entities = [FirstMeetSong::class, LocalSong::class, HistorySong::class, DownloadSong::class, LoveSong::class,OnlineSong::class],
    version = 1,
    exportSchema = false
)
abstract class MaiDatabase : RoomDatabase() {
    abstract fun localSongDao(): LocalSongDao
    abstract fun historySongDao(): HistorySongDao
    abstract fun downloadSongDao(): DownloadSongDao
    abstract fun loveSongDao(): LoveSongDao
    companion object {
        @Volatile
        private var INSTANCE: MaiDatabase? = null
        fun getDatabase(): MaiDatabase {
            val tempInstance = INSTANCE
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