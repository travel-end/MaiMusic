package wind.maimusic.room

import androidx.room.*
import wind.maimusic.model.searchhot.HistoryTag
import wind.maimusic.model.searchhot.HotSearchSong
import wind.maimusic.model.searchhot.RecommendSearch

/**
 * @By Journey 2020/11/11
 * @Description
 */
@Dao
interface SearchSongDao {
    @Insert(entity = RecommendSearch::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecommendSearch(recommendSearch:List<RecommendSearch>):List<Long>

    @Insert(entity = HotSearchSong::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHotSearchSongs(hotSearchSongs:List<HotSearchSong>):List<Long>

    @Query("SELECT * FROM t_recommend_search ORDER BY RANDOM() LIMIT (:count)")
    suspend fun findRandomRecommendSearch(count:Int):List<RecommendSearch>

    @Query("SELECT * FROM t_hot_search ORDER BY RANDOM() LIMIT (:count)")
    suspend fun findRandomHotSearchSongs(count: Int):List<HotSearchSong>

    @Insert(entity = HistoryTag::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOneSearchTag(tag:HistoryTag):Long

    @Query("SELECT * FROM t_search_history ORDER BY id DESC LIMIT(:count)")
    suspend fun findRecentSearchHistory(count: Int):List<HistoryTag>

    @Query("SELECT * FROM t_search_history WHERE name=(:tagName) ORDER BY id")
    suspend fun findSearchTagByName(tagName:String):List<HistoryTag>

    @Query("DELETE FROM t_search_history")
    suspend fun deleteSearchHistoryTag():Int

}