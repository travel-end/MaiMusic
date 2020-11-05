package wind.maimusic.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wind.maimusic.model.listensong.ListenBanner

@Dao
interface ListenBannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE,entity = ListenBanner::class)
    suspend fun addListenBanners(banners:List<ListenBanner>)

    @Query("SELECT * FROM listen_banner WHERE id <= (:utilId) ORDER BY id")
    suspend fun getStartBanners(utilId:Int):List<ListenBanner>

    @Query("SELECT * FROM listen_banner WHERE id BETWEEN (:start) AND (:end) ORDER BY id")
    suspend fun getRangeBanners(start:Int,end:Int):List<ListenBanner>
}