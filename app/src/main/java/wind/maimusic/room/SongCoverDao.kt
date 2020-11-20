package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.listensong.SongListCover

@Dao
interface SongCoverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SongListCover::class)
    suspend fun addSongCovers(songCovers:List<SongListCover>)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SongListCover::class)
    suspend fun addSongCover(songCovers:SongListCover):Long

    @Query("SELECT * FROM song_list_cover WHERE id <= (:utilId) ORDER BY id")
    suspend fun getStartListCovers(utilId:Int):List<SongListCover>

    @Query("SELECT * FROM song_list_cover ORDER BY RANDOM() LIMIT (:count)")
    suspend fun getRandomListCovers(count:Int):List<SongListCover>

    @Query("SELECT * FROM song_list_cover WHERE type=(:type) ORDER BY id")
    suspend fun findSongListByType(type:Int):List<SongListCover>

    @Query("SELECT * FROM song_list_cover WHERE isUserCreated=1 ORDER BY id")
    suspend fun findAllUserCreatedSongList():List<SongListCover>

    @Query("SELECT * FROM song_list_cover WHERE id=(:id)")
    suspend fun findSongCoverById(id:Int):SongListCover

    @Delete(entity = SongListCover::class)
    suspend fun deleteUserCreatedSongList(songCovers: SongListCover):Int
}