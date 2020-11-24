package wind.maimusic.service.ofnew

/**
 * @By Journey 2020/11/24
 * @Description
 */
object ServiceConstants {
    const val TRACK_WENT_TO_NEXT = 2//下一首
    const val RELEASE_WAKELOCK = 3//播放完成
    const val TRACK_PLAY_ENDED = 4//播放完成
    const val TRACK_PLAY_ERROR = 5//播放出错
    const val PREPARE_ASYNC_UPDATE = 7//PrepareAsync装载进程
    const val PLAYER_PREPARED = 8//mediaplayer准备完成
    const val AUDIO_FOCUS_CHANGE = 12//音频焦点改变
    const val VOLUME_FADE_DOWN = 13//音量改变减少
    const val VOLUME_FADE_UP = 14//音量改变增加
    const val NOTIFICATION_ID =0x123
}