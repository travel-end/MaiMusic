package wind.maimusic.ui.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_collect.*
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.songlist.SongListItem
import wind.maimusic.ui.adapter.LateralSpreadsAdapter
import wind.maimusic.ui.adapter.OnLsItemClickListener
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
    private var isSongListEmpty:Boolean = false
    private var deletePosition:Int = -1
    private lateinit var createdSongListAdapter:LateralSpreadsAdapter
    companion object {
        fun newInstance():Fragment{
            return FavoritesFragment()
        }
    }
    override fun layoutResId()=R.layout.fragment_collect

    override fun initView() {
        super.initView()
        rvCreateNewSongList = mRootView.findViewById(R.id.collect_rv_create_new_song_list2)
        createdSongListAdapter = LateralSpreadsAdapter(requireContext())
        createdSongListAdapter.setOnLsItemClickListener(object :
            OnLsItemClickListener<SongListItem> {
            override fun onItemClick(type: Int,t: SongListItem,position:Int) {
                if (type == LateralSpreadsAdapter.TYPE_EDIT) {
                    LogUtil.e("---edit")
                } else if (type == LateralSpreadsAdapter.TYPE_DELETE) {
                    LogUtil.e("---TYPE_DELETE")
                    // TODO: 2020/11/8 添加确认删除弹窗
                    deletePosition = position
                    mViewModel.deleteCreatedSongList(t)

                }
            }

            override fun onHoldItemClick(t: SongListItem) {
                LogUtil.e("---SongListItem${t.title}")
                // TODO: 2020/11/8 使用ImageSelector选取封面图片
            }
        })
    }

    override fun initData() {
        super.initData()
        mViewModel.getSongNums()
        mViewModel.findAllCreatedSongList()
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
        mViewModel.allCreatedSongList.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                createdSongListAdapter.mDataList = it.toMutableList()
                rvCreateNewSongList.adapter = createdSongListAdapter
                rvCreateNewSongList.visible()
            } else {
                isSongListEmpty = true
            }
        })
        mViewModel.addAndFindThisSongList.observe(this,Observer{
            if (it != null) {
                if (isSongListEmpty) {
                    isSongListEmpty = false
                    val songList = mutableListOf<SongListItem>()
                    songList.add(it)
                    createdSongListAdapter.mDataList =songList
                    rvCreateNewSongList.adapter = createdSongListAdapter
                    rvCreateNewSongList.visible()
                } else {
                    createdSongListAdapter.addDataRecent(it)
                }
            }
        })
        mViewModel.deleteCreatedSongList.observe(this,Observer{
            if (it == true) {
                createdSongListAdapter.removeData(deletePosition)
            }
        })
        Bus.observe<String>(Constants.LOGIN_SUCCESS,this) {
            LogUtil.e("---FavoritesFragment--login success:$it")
            collect_tv_cuter.text = "提莫队长"
        }
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
        collect_layout_to_login.fastClickListener {
            if (isLogin()) {
                it.nav(R.id.to_mime_fragment)
            } else {
                requireActivity().toLogin()
            }
        }
        collect_iv_create_new_song_list.fastClickListener {
            if (createNewSongListDialog == null) {
                createNewSongListDialog = CreateNewSongListDialog(requireContext(),object :OnDialogBtnClickListener{
                    override fun onBtnClick(text: String) {
                        LogUtil.e("tetx:$text")
                        requireActivity().hideKeyboards()
                        val songListItem = SongListItem(
                            title = text
                        )
                        mViewModel.addAndFindThisSongList(songListItem)

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