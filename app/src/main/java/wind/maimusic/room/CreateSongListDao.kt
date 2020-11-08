package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.songlist.SongListItem

@Dao
interface CreateSongListDao {

    @Insert(entity = SongListItem::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCreatedSongList(songList:SongListItem):Long

    @Update(entity = SongListItem::class)
    suspend fun updateCreatedSongList(songList: SongListItem)

    @Query("DELETE FROM t_created_song_list WHERE id=(:id)")
    suspend fun deleteCreatedSongListById(id:Int):Int

    @Query("SELECT * FROM t_created_song_list WHERE id=(:id) LIMIT 1")
    suspend fun findCreatedSongListById(id:Int):SongListItem

    @Query("SELECT * FROM t_created_song_list")
    suspend fun findALlCreatedSongList():List<SongListItem>

    @Delete(entity = SongListItem::class)
    suspend fun deleteSongList(songList: SongListItem):Int

    @Query("UPDATE t_created_song_list SET title=(:name) AND cover=(:cover) WHERE id=(:id)")
    suspend fun updateSongListNameAndCover(name:String,cover:String,id:Int)
}