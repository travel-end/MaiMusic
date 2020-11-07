package wind.maimusic.ui.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_collect.*
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.songlist.SongListItem
import wind.maimusic.ui.adapter.LateralSpreadsAdapter
import wind.maimusic.utils.*
import wind.maimusic.vm.FavoritesViewModel
import wind.maimusic.widget.dialog.CreateNewSongListDialog
import wind.maimusic.widget.dialog.OnDialogBtnClickListener
import wind.widget.utils.fastClickListener

/**
 * 我的收藏
 */
class FavoritesFragment:BaseLifeCycleFragment<FavoritesViewModel>() {
    private var createNewSongListDialog:CreateNewSongListDialog?=null
    private lateinit var rvCreateNewSongList:RecyclerView
    companion object {
        fun newInstance():Fragment{
            return FavoritesFragment()
        }
    }
    override fun layoutResId()=R.layout.fragment_collect

    override fun initView() {
        super.initView()
        rvCreateNewSongList = mRootView.findViewById(R.id.collect_rv_create_new_song_list2)
        val list = mutableListOf<SongListItem>()
        val cover = "https://cdnmusic.migu.cn/picture/2020/1027/1021/AS9b631a8fddf7ccc36362b6925a9b4f1e.jpg"
        for (i in 0..4) {
            val item = SongListItem(
                title = "fff",
                cover = cover,
                readNum = 4
            )
            list.add(item)
        }
        val adapert = LateralSpreadsAdapter(requireContext(),list)
        rvCreateNewSongList.adapter = adapert
        rvCreateNewSongList.visible()
    }

    override fun initData() {
        super.initData()
        mViewModel.getSongNums()
    }

    override fun observe() {
        super.observe()
        mViewModel.songNums.observe(this,Observer{
            it?.let {
                val loved = it[0]
                if (loved!= null) {
                    collect_tv_loved_songs.text = "${loved}首"
                }
            }
            
        })
    }

    override fun initAction() {
        super.initAction()
        collect_tv_local_song.fastClickListener {
            it.nav(R.id.to_local_song_fragment)
        }
        collect_tv_recent_read.fastClickListener {
            it.nav(R.id.to_recent_read_fragment)
        }
        collect_tv_recent_listen.fastClickListener {
            it.nav(R.id.to_recent_listen_fragment)
        }
        collect_tv_downloaded.fastClickListener {
            it.nav(R.id.to_downloaded_fragment)
        }
        collect_fl_loved_song.fastClickListener {
            it.nav(R.id.to_loved_song_fragment)
        }
        collect_fl_loved_poetry.fastClickListener {
            it.nav(R.id.to_loved_poetry_fragment)
        }
        collect_iv_create_new_song_list.fastClickListener {
            if (createNewSongListDialog == null) {
                createNewSongListDialog = CreateNewSongListDialog(requireContext(),object :OnDialogBtnClickListener{
                    override fun onBtnClick(text: String) {
                        LogUtil.e("tetx:$text")
                        requireActivity().hideKeyboards()
                    }
                    override fun onCancelClick() {
                        requireActivity().hideKeyboards()
                    }
                })
            }
            createNewSongListDialog?.show()
            createNewSongListDialog?.etContent.showKeyBoard(requireContext())
        }
    }
}