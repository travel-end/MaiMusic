package wind.maimusic.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import wind.maimusic.MaiApp
import wind.maimusic.model.*
import wind.maimusic.model.firstmeet.FirstMeetSong

/**
 * @By Journey 2020/10/28
 * @Description
 */
@Database(entities = [FirstMeetSong::class,LocalSong::class,HistorySong::class,DownloadSong::class,LoveSong::class],version = 1)
abstract class MaiDatabase:RoomDatabase() {
    abstract fun firstMeetSongDao():FirstMeetSongDao
    abstract fun localSongDao():LocalSongDao
    abstract fun historySongDao():HistorySongDao
    abstract fun downloadSongDao():DownloadSongDao
    abstract fun loveSongDao():LoveSongDao
    companion object {
        @Volatile
        private var INSTANCE:MaiDatabase?=null
        fun getDatabase():MaiDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    MaiApp.getInstance().applicationContext,
                    MaiDatabase::class.java,
                    "maimusic_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}