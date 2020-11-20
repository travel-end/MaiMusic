package wind.maimusic.room.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import wind.maimusic.MaiApp
import wind.maimusic.model.JustOnlineSong
import wind.maimusic.model.OnlineSong
import wind.maimusic.model.listensong.*
import wind.maimusic.model.singer.Singer
import wind.maimusic.room.*

/**
 * @By Journey 2020/10/30
 * @Description 在线歌单数据库
 * FirstMeetSong:第一次打开app时推荐歌曲
 * OnlineSong:通用在线歌单列表（每次添加歌曲时会把之前储存的歌曲全部删除，伴随较多的删除和增加操作）
 *
 */
@Database(
    entities = [
        OnlineSong::class,
        ListenBanner::class,
        SongListCover::class,
        Singer::class,
        JustOnlineSong::class
    ],
    version = 1,
    exportSchema = false
)
abstract class OnlineSongDatabase : RoomDatabase() {
    // app歌库音乐
    abstract fun onlineSongDao(): OnlineSongDao
    // 首页banner
    abstract fun listenBannerDao(): ListenBannerDao
    // 歌单封面信息
    abstract fun songListCoverDao(): SongCoverDao
    // 用户创建的歌单
//    abstract fun createdSongListDao():CreateSongListDao
    // 歌手
    abstract fun singerDao():SingerDao
    // 一次性歌单（如专辑音乐）
    abstract fun justOnlineSongDao():JustOnlineSongDao

    companion object {
        @Volatile
        private var INSTANCE: OnlineSongDatabase? = null
        fun getDatabase(): OnlineSongDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    MaiApp.getInstance().applicationContext,
                    OnlineSongDatabase::class.java,
                    "database_online_song_list"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}