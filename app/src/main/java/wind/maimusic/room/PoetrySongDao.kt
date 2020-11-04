package wind.maimusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wind.maimusic.model.listensong.PoetrySong

@Dao
interface PoetrySongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = PoetrySong::class)
    suspend fun addPoetrySongs(poetrySongs:List<PoetrySong>)

//    @Query("SELECT * FROM poetry_song WHERE id >= (SELECT floor(RAND() * (SELECT MAX(id) FROM poetry_song))) ORDER BY id LIMIT 8")
//    suspend fun getRandomPoetrySongs():List<PoetrySong>

    @Query("SELECT * FROM poetry_song WHERE id <= (:utilId) ORDER BY id")
    suspend fun getStartPoetrySongs(utilId:Int):List<PoetrySong>

    @Query("SELECT * FROM poetry_song WHERE id BETWEEN (:start) AND (:end) ORDER BY id")
    suspend fun getRangePoetrySongs(start:Int,end:Int):List<PoetrySong>
}