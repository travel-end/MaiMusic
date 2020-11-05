package wind.maimusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wind.maimusic.model.listensong.SingleSong

@Dao
interface SingleSongDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SingleSong::class)
//    suspend fun addSingleSongs(singleSongs:List<SingleSong>)
//
//    @Query("SELECT * FROM single_song WHERE id <= (:utilId) ORDER BY id")
//    suspend fun getStartSingleSongs(utilId:Int):List<SingleSong>
//
//    @Query("SELECT * FROM single_song WHERE id BETWEEN (:start) AND (:end) ORDER BY id")
//    suspend fun getRangeSingleSongs(start:Int,end:Int):List<SingleSong>
}