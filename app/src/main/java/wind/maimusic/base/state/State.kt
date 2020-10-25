package wind.maimusic.base.state

import androidx.annotation.DrawableRes


/**
 * @By Journey 2020/10/25
 * @Description
 */
data class State(
    val code:StateType,
    val msg:String = "",
    @DrawableRes
    val res:Int = 0
)