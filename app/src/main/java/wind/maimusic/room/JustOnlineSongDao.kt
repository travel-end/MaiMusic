package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.JustOnlineSong

@Dao
interface JustOnlineSongDao {
    @Insert(entity = JustOnlineSong::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJustOnlineSongs(onlineSongs:List<JustOnlineSong>):List<Long>

    @Insert(entity = JustOnlineSong::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSingleJustOnlineSong(justOnlineSong: JustOnlineSong):Long

    @Query("SELECT * FROM t_just_online_song ORDER BY id")
    suspend fun findJustOnlineSongs():List<JustOnlineSong>

    @Query("DELETE FROM t_just_online_song")
    suspend fun deleteAllJustOnlineSong()

    @Query("DELETE FROM t_just_online_song WHERE songId=(:songId)")
    suspend fun deleteSingleJustOnlineSong(songId:String):Int

    @Delete(entity = JustOnlineSong::class)
    suspend fun deleteSingleJustOnlineSong(justOnlineSong: JustOnlineSong):Int
}