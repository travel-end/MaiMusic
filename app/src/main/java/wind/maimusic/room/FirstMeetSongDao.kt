package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.firstmeet.FirstMeetSong

@Dao
interface FirstMeetSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = FirstMeetSong::class)
    suspend fun addFirstMeetSong(firstMeetSong: FirstMeetSong)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = FirstMeetSong::class)
    suspend fun addFirstMeetSongList(firstMeetSongs: List<FirstMeetSong>)

    @Query("SELECT * FROM first_meet_song ORDER BY id")
    suspend fun findAllFirstSong(): List<FirstMeetSong>

    @Delete(entity = FirstMeetSong::class)
    suspend fun deleteAllFirstMeetSong(firstMeetSongs: List<FirstMeetSong>)
}