package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.DownloadSong

/**
 * @By Journey 2020/10/28
 * @Description
 */
@Dao
interface DownloadSongDao {
    @Insert(entity = DownloadSong::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDownloadSong(downloadSong: DownloadSong): Long

    @Delete
    suspend fun deleteDownloadSong(downloadSong: DownloadSong): Int

    @Query("SELECT *FROM down_song ORDER BY id")
    suspend fun findAllDownloadSong(): List<DownloadSong>
}