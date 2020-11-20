package wind.maimusic.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.model.core.AlListBean
import wind.maimusic.model.listensong.SongListCover
import wind.maimusic.model.singer.Singer
import wind.maimusic.ui.activities.LoginActivity
import wind.maimusic.ui.activities.PlayActivity
import wind.widget.cost.Consts

/**
 * @By Journey 2020/11/20
 * @Description
 */
object NavUtil {


    /*去播放页面*/
    fun toPlayAct(activity: Activity, playStatus: Int) {
        val playIntent = Intent(activity, PlayActivity::class.java)
        playIntent.putExtra(Consts.PLAY_STATUS, playStatus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(
                playIntent,
                ActivityOptions.makeSceneTransitionAnimation(activity)
                    .toBundle()
            )
        } else {
            activity.startActivity(playIntent)
        }
    }

    // 关于Navigation: https://juejin.im/post/6844904180990246926
    /*去歌单页面（今日推荐）*/
    fun recommendToSongList(view: View?, songListType: Int) {
        if (view != null) {
            val bundle = Bundle()
            bundle.putString(Constants.SONG_LIST_TYPE, songListType.toString())
            Navigation.findNavController(view).navigate(R.id.to_song_list_fragment, bundle)
        }
    }

    /*去歌手歌单页面*/
    fun singerToSongList(view: View?, singer: Singer) {
        if (view != null) {
            val bundle = bundleOf(
                Constants.SINGER_NAME to singer.name,
                Constants.SINGER_ID to singer.singerId.toString(),
                Constants.SINGER_COVER to singer.cover
            )
            Navigation.findNavController(view).navigate(R.id.singer_to_song_list_fragment, bundle)
        }
    }
    /*去用户创建的歌单页面*/
    fun toCreatedSongList(view: View?, cover: SongListCover) {
        if (view != null) {
            val bundle = bundleOf(
                Constants.SONG_LIST_ID to cover.type,
                Constants.SONG_LIST_NAME to cover.listName,
                Constants.SONG_LIST_COVER to cover.cover
            )
            Navigation.findNavController(view).navigate(R.id.to_create_song_list_fragment, bundle)
        }
    }

    /**
     * @sample
     * 去专辑歌单
     */
    fun albumToSongList(
        view: View?,
        album: AlListBean
    ) {
        if (view != null) {
            val bundle = Bundle().apply {
                putString(Constants.ALBUM_ID, album.albumMID)
                putString(Constants.ALBUM_NAME, album.albumName)
                putString(Constants.ALBUM_COVER, album.albumPic)
                putString(Constants.ALBUM_SINGER, album.singerName)
                putString(Constants.ALBUM_PUBLIC_TIME, album.publicTime)
            }
            Navigation.findNavController(view).navigate(R.id.to_album_song_fragment, bundle)
        }
    }

    fun nav(view: View?, id: Int, bundle: Bundle? = null) {
        if (view != null) {
            if (bundle != null) {
                Navigation.findNavController(view).navigate(id, bundle)
            } else {
                Navigation.findNavController(view).navigate(id)
            }
        }
    }

    fun navUp(view: View?) {
        if (view != null) {
            Navigation.findNavController(view).navigateUp()
        }
    }

    fun toLogin(activity: Activity?) {
        if (activity != null) {
            val loginIntent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(loginIntent)
        }
    }
}