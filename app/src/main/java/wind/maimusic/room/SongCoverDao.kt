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

    @Query("SELECT * FROM song_list_cover WHERE id <= (:utilId) ORDER BY id")
    suspend fun getStartListCovers(utilId:Int):List<SongListCover>

    @Query("SELECT * FROM song_list_cover ORDER BY RANDOM() LIMIT (:count)")
    suspend fun getRandomListCovers(count:Int):List<SongListCover>
}