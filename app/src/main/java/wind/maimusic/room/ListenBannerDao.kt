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

    // 4,7
    @Query("SELECT * FROM listen_banner WHERE id >= (SELECT floor(RAND() * (SELECT MAX(id) FROM listen_banner))) ORDER BY id LIMIT 5")
    suspend fun getRandomBanners():List<ListenBanner>
}