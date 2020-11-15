package wind.maimusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wind.maimusic.model.singer.Singer

@Dao
interface SingerDao {
    @Insert(entity = Singer::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSingers(singers:List<Singer>):List<Long>

    @Query("SELECT * FROM t_singer ORDER BY id")
    suspend fun findAllSingers():List<Singer>

    @Query("SELECT * FROM t_singer WHERE singerId=(:singerId)")
    suspend fun findSingerBySingerId(singerId:Int):Singer

    @Query("SELECT * FROM t_singer WHERE sex=(:sex) ORDER BY id")
    suspend fun findSingersBySex(sex:Int):List<Singer>


}