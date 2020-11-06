package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.OnlineSong

/**
 * @By Journey 2020/10/30
 * @Description
 * 范围查询：in(x,x); between x and x;  not in(x,x)
 * 分页查询limit beginIndex,pageSize，如
 * ex：每页显示3条数据
第一页: SELECT * FROM 表名 LIMIT 0,3    --0,1,2
第二页: SELECT * FROM 表名 LIMIT 3,3    --3,4,5
第三页: SELECT * FROM 表名 LIMIT 6,3    --6,7,8
第四页: SELECT * FROM 表名 LIMIT 9,3    --9,10,11
……
第七页: SELECT * FROM 表名 LIMIT 18,3   --18,19,20
 */
@Dao
interface OnlineSongDao {
    @Insert(entity = OnlineSong::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOnlineSong(onlineSong: OnlineSong)

    @Insert(entity = OnlineSong::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOnlineSongs(onlineSongs: List<OnlineSong>):List<Long>

    @Query("SELECT * FROM online_song ORDER BY id")
    suspend fun findOnlineSongList(): List<OnlineSong>

    @Delete(entity = OnlineSong::class)
    suspend fun deleteOnlineSongs(onlineSongs: List<OnlineSong>)

    @Query("DELETE FROM online_song")
    suspend fun deleteAllOnlineSongs()

    @Query("SELECT * FROM online_song ORDER BY RANDOM() LIMIT (:count)")
    suspend fun findRandomSingleSong(count: Int): List<OnlineSong>

    @Query("SELECT * FROM online_song ORDER BY RANDOM() LIMIT 1")
    suspend fun findRandomSingleSong(): OnlineSong?

    @Query("SELECT * FROM online_song WHERE id BETWEEN(:startIndex) AND (:endIndex) ORDER BY id")
    suspend fun findRangeOnlineSongs(startIndex:Int,endIndex:Int):List<OnlineSong>

    @Query("SELECT * FROM online_song WHERE mainType=(:type) ORDER BY id")
    suspend fun findOnlineSongByMainType(type: Int): List<OnlineSong>

    @Query("SELECT * FROM online_song WHERE mainType=(:mainType) AND secondType=(:secondType) ORDER BY id")
    suspend fun findOnlineSongByMainAndSecondType(mainType: Int, secondType: Int): List<OnlineSong>

    @Query("SELECT * FROM online_song WHERE isPoetrySong=1 ORDER BY id LIMIT (:count)")
    suspend fun findOnlinePoetrySong(count: Int): List<OnlineSong>

    @Query("SELECT * FROM online_song WHERE id=1")
    suspend fun findLaunchSong(): OnlineSong?

    @Query("SELECT * FROM online_song ORDER BY id LIMIT 0,10")
    suspend fun findLaunchSongs(): List<OnlineSong>
}