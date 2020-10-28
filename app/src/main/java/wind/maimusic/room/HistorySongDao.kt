package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.HistorySong

/**
 * @By Journey 2020/10/28
 * @Description
 */
@Dao
interface HistorySongDao {
    @Insert(entity = HistorySong::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistorySong(historySong: HistorySong):Long

    @Delete(entity = HistorySong::class)
    suspend fun deleteHistorySong(historySong: HistorySong):Int

    @Query("SELECT * FROM history_song ORDER BY id")
    suspend fun findAllHistorySong():List<HistorySong>

    @Query("DELETE FROM history_song")
    suspend fun deleteAllHistorySong():Int
}