package wind.maimusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wind.maimusic.model.listensong.SingleSong

@Dao
interface SingleSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SingleSong::class)
    suspend fun addSingleSongs(singleSongs:List<SingleSong>)

    @Query("SELECT * FROM single_song WHERE id >= (SELECT floor(RAND() * (SELECT MAX(id) FROM single_song))) ORDER BY id LIMIT 3")
    suspend fun getRandomSingleSongs():List<SingleSong>
}