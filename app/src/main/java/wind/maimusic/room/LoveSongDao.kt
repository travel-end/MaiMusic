package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.LoveSong

/**
 * @By Journey 2020/10/28
 * @Description
 */
@Dao
interface LoveSongDao {
    @Insert(entity = LoveSong::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLoveSong(loveSong: LoveSong):Long

    @Delete
    suspend fun deleteLoveSong(loveSong: LoveSong):Int

    @Query("SELECT * FROM love_song WHERE songId =(:songId)")
    suspend fun findLoveSong(songId:String):List<LoveSong>

    @Query("SELECT * FROM love_song ORDER BY id")
    suspend fun findAllLoveSong():List<LoveSong>
}