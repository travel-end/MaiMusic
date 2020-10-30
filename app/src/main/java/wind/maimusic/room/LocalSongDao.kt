package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.LocalSong

/**
 * @By Journey 2020/10/28
 * @Description 插入返回Long 值 代表的是这条数据表中的主键
 * 删除数据返回的Int 值 表示的是删除成功的条数
 * 自增主键从1开始
 */
@Dao
interface LocalSongDao {
    // onConflict.REPLACE:如果表中已有数据 就覆盖掉  返回list<Long>为表中数据的主键集合
    @Insert(entity = LocalSong::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocalSongs(localSongs:List<LocalSong>):List<Long>

    @Insert(entity = LocalSong::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocalSong(localSong:LocalSong):Long

    @Delete(entity = LocalSong::class)
    suspend fun deleteLocalSong(localSong:LocalSong):Int

    // 返回Int表示删除成功的条数
    @Delete(entity = LocalSong::class)
    suspend fun deleteLocalSongList(localSongs: List<LocalSong>):Int

    @Query("DELETE FROM local_song")
    suspend fun deleteAllLocalSong()

    @Query("SELECT * FROM local_song ORDER BY id")
    suspend fun findAllLocalSong():List<LocalSong>

    // 更新本地音乐的封面图片
    @Query("UPDATE local_song  SET pic=(:pic) WHERE songId=(:songId)")
    suspend fun updateLocalSongCoverBySongId(pic:String,songId:String)
}