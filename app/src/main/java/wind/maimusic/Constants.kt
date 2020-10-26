package wind.maimusic

/**
 * @By Journey 2020/10/25
 * @Description
 */
object Constants {


    // 保存封面图片的路径
    val STORAGE_IMG_FILE =
        MaiApp.getInstance().getExternalFilesDir("").toString() + "/maimusic/image/"
    // 保存下载歌曲的路径
    val STORAGE_SONG_FILE =
        MaiApp.getInstance().getExternalFilesDir("").toString() + "/maimusic/download/"
    // 保存歌词的路径
    val STORAGE_LRC_FILE =
        MaiApp.getInstance().getExternalFilesDir("").toString() + "/maimusic/lrc/"
}