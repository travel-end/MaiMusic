package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.firstmeet.FirstMeetSong

@Dao
interface FirstMeetSongDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = FirstMeetSong::class)
//    suspend fun addFirstMeetSong(firstMeetSong: FirstMeetSong)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = FirstMeetSong::class)
//    suspend fun addFirstMeetSongList(firstMeetSongs: List<FirstMeetSong>)
//
//    @Query("SELECT * FROM first_meet_song ORDER BY id")
//    suspend fun findAllFirstSong(): List<FirstMeetSong>
//
//    @Query("SELECT * FROM first_meet_song WHERE id=(:id)")
//    suspend fun findFirstMeetSongById(id:Int):List<FirstMeetSong>
//
//    @Query("DELETE FROM first_meet_song")
//    suspend fun deleteAllFirstMeetSong()
}