//package wind.maimusic.room
//
//import androidx.room.*
//import wind.maimusic.model.cuter.Cuter
//
///**
// * @By Journey 2020/11/18
// * @Description
// */
//@Dao
//interface CuterDao {
//    @Insert(entity = Cuter::class,onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addOneCuter(cuter: Cuter):Long
//
//    @Delete(entity = Cuter::class)
//    suspend fun deleteCurrentCuter(cuter: Cuter):Int
//
//    @Query("DELETE FROM t_cuter WHERE cuterId=(:cuterId)")
//    suspend fun deleteCuterById(cuterId:Int):Int
//
//    @Query("DELETE FROM t_cuter")
//    suspend fun deleteCuter()
//
//    @Query("SELECT * FROM t_cuter ORDER BY id")
//    suspend fun findCuter():List<Cuter>
//}