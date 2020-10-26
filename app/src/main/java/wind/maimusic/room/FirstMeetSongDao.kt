package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.FirstMeetSong

@Dao
interface FirstMeetSongDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE,entity = FirstMeetSong::class)
    suspend fun addFirstMeetSong(firstMeetSong: FirstMeetSong)
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE,entity = FirstMeetSong::class)
    suspend fun addFirstMeetSongList(firstMeetSongs: List<FirstMeetSong>)
    @Transaction
    @Query("SELECT * FROM first_meet_song ORDER BY id")
    suspend fun findAllFirstSong():MutableList<FirstMeetSong>
    @Transaction
    @Delete(entity = FirstMeetSong::class )
    suspend fun deleteAllFirstMeetSong(firstMeetSongs: List<FirstMeetSong>)
}