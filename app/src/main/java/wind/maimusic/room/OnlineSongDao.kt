package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.OnlineSong

/**
 * @By Journey 2020/10/30
 * @Description
 */
@Dao
interface OnlineSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = OnlineSong::class)
    suspend fun addOnlineSong(onlineSong: OnlineSong)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = OnlineSong::class)
    suspend fun addOnlineSongs(onlineSongs: List<OnlineSong>)

    @Query("SELECT * FROM online_song ORDER BY id")
    suspend fun findOnlineSongList(): List<OnlineSong>

    @Delete(entity = OnlineSong::class)
    suspend fun deleteOnlineSongs(onlineSongs: List<OnlineSong>)

    @Query("DELETE FROM online_song")
    suspend fun deleteAllOnlineSongs()
}