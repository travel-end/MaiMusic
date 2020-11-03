package wind.maimusic.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleActivity
import wind.maimusic.room.database.OnlineSongDatabase
import wind.maimusic.utils.AssetsUtil
import wind.maimusic.utils.GlobalUtil
import wind.maimusic.utils.SpUtil
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.vm.WelcomeViewModel
import wind.widget.cost.Consts

/**
 * @By Journey 2020/10/25
 * @Description
 */
class WelcomeActivity:BaseLifeCycleActivity<WelcomeViewModel>() {
    companion object {
        const val mPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val mRequestCode = 10
    }
    override fun layoutResId()= R.layout.activity_welcome
    override fun initStatusBar() {
        ImmersionBar.with(this).transparentBar().init()
    }
    override fun initData() {
        super.initData()
        if (ContextCompat.checkSelfPermission(this,
                mPermission
            )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(mPermission),
                mRequestCode
            )
        } else {
            window.decorView.postDelayed({
                toMain()
            },800)
        }
    }
    private fun toMain() {
        initDatabase()
        startActivity(
            Intent(this,
            MainActivity::class.java)
        )
        finish()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == mRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toMain()
            } else {
                Toast.makeText(this, "拒绝该权限无法使用该程序", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun initDatabase(){
        if (SpUtil.getString(Consts.FIRST_LAUNCH).isEmpty()) {
            GlobalUtil.execute {
                val songs = AssetsUtil.loadFirstMeetSongs()
                if (isNotNullOrEmpty(songs)) {
                    OnlineSongDatabase.getDatabase().firstMeetSongDao().addFirstMeetSongList(songs!!)
                }
                AssetsUtil.initAppData()
                SpUtil.saveValue(Consts.FIRST_LAUNCH,"has_launch")
            }
        }
    }
}