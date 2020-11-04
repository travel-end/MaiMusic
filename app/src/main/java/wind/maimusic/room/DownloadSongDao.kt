package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.download.DownloadSong

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

    @Query("DELETE FROM down_song WHERE songId=(:songId)")
    suspend fun deleteDownloadedBySongId(songId: String): Int

    @Query("SELECT * FROM down_song ORDER BY id")
    suspend fun findAllDownloadSong(): List<DownloadSong>

    @Query("SELECT * FROM down_song WHERE songId=(:songId)")
    suspend fun findDownloadedSongBySongId(songId: String): List<DownloadSong>

    @Query("SELECT * FROM down_song WHERE songId=(:songId)")
    suspend fun findIdBySongId(songId: String): List<DownloadSong>

    @Query("SELECT * FROM down_song WHERE id>(:id)")
    suspend fun findDownloadSongById(id: Long): List<DownloadSong>

    @Query("UPDATE down_song SET position=(:position) WHERE songId=(:songId)")
    suspend fun updateDownloadSongPositionBySongId(position: Int, songId: String)

    @Query("UPDATE down_song SET status=(:status) WHERE songId=(:songId)")
    suspend fun updateDownloadSongStatusBySongId(status: Int, songId: String)
}