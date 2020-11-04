package wind.maimusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wind.maimusic.model.listensong.SongListCover

@Dao
interface SongCoverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SongListCover::class)
    suspend fun addSongCovers(songCovers:List<SongListCover>)

//    @Query("SELECT * FROM song_list_cover WHERE id >= (SELECT floor(RAND() * (SELECT MAX(id) FROM song_list_cover))) ORDER BY id LIMIT 6")
//    suspend fun getRandomCovers():List<SongListCover>

    @Query("SELECT * FROM song_list_cover WHERE id <= (:utilId) ORDER BY id")
    suspend fun getStartListCovers(utilId:Int):List<SongListCover>

    @Query("SELECT * FROM song_list_cover WHERE id BETWEEN (:start) AND (:end) ORDER BY id")
    suspend fun getRangeSongListCovers(start:Int,end:Int):List<SongListCover>
}