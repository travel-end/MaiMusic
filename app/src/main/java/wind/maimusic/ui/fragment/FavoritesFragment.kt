package wind.maimusic.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_collect.*
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.listensong.SongListCover
import wind.maimusic.ui.adapter.LateralSpreadsAdapter
import wind.maimusic.ui.adapter.OnLsItemClickListener
import wind.maimusic.utils.*
import wind.maimusic.vm.FavoritesViewModel
import wind.maimusic.widget.dialog.CreateNewSongListDialog
import wind.maimusic.widget.dialog.OnDialogBtnClickListener
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg

/**
 * 我的收藏
 */
class FavoritesFragment : BaseLifeCycleFragment<FavoritesViewModel>() {
    private var createNewSongListDialog: CreateNewSongListDialog? = null
    private lateinit var rvCreateNewSongList: RecyclerView
    private var isSongListEmpty: Boolean = false
    private var deletePosition: Int = -1
    private lateinit var createdSongListAdapter: LateralSpreadsAdapter

    companion object {
        fun newInstance(): Fragment {
            return FavoritesFragment()
        }
    }

    override fun layoutResId() = R.layout.fragment_collect

    override fun initView() {
        super.initView()
        rvCreateNewSongList = mRootView.findViewById(R.id.collect_rv_create_new_song_list2)
        // 包含右滑的recyclerView的适配器
        createdSongListAdapter = LateralSpreadsAdapter(requireContext())
        createdSongListAdapter.setOnLsItemClickListener(object :
            OnLsItemClickListener<SongListCover> {
            override fun onItemClick(type: Int, t: SongListCover, position: Int) {
                if (type == LateralSpreadsAdapter.TYPE_EDIT) {
                    LogUtil.e("---edit")
                } else if (type == LateralSpreadsAdapter.TYPE_DELETE) {
                    LogUtil.e("---TYPE_DELETE")
                    // TODO: 2020/11/8 添加确认删除弹窗
                    deletePosition = position
                    mViewModel.deleteCreatedSongList(t)
                }
            }

            override fun onHoldItemClick(view:View,t: SongListCover) {
                LogUtil.e("---SongListItem${t.listName}")
                NavUtil.toCreatedSongList(mRootView,t)
            }
        })

        val cuter = CuterManager.getCuterInfo()
        if (cuter != null) {
            val cuterName = cuter.nickName
            collect_tv_nickname.text = cuterName
            collect_iv_avatar.loadImg(cuter.cuterCover ?: "", error = R.drawable.un_login)
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getSongNums()
        mViewModel.findAllCreatedSongList()
    }

    override fun observe() {
        super.observe()
        mViewModel.songNums.observe(this, Observer {
            it?.let {
                val loved = it[0]
                if (loved != null) {
                    collect_tv_loved_songs.text = "${loved}首"
                }
            }
        })
        mViewModel.allCreatedSongList.observe(this, Observer {
            if (isNotNullOrEmpty(it)) {
                createdSongListAdapter.mDataList = it.toMutableList()
                rvCreateNewSongList.adapter = createdSongListAdapter
                rvCreateNewSongList.visible()
            } else {
                isSongListEmpty = true
            }
        })
        mViewModel.addAndFindThisSongList.observe(this, Observer {
            if (it != null) {
                if (isSongListEmpty) {
                    isSongListEmpty = false
                    val songList = mutableListOf<SongListCover>()
                    songList.add(it)
                    createdSongListAdapter.mDataList = songList
                    rvCreateNewSongList.adapter = createdSongListAdapter
                    rvCreateNewSongList.visible()
                } else {
                    createdSongListAdapter.addDataRecent(it)
                }
            }
        })
        mViewModel.deleteCreatedSongList.observe(this, Observer {
            if (it == true) {
                createdSongListAdapter.removeData(deletePosition)
            }
        })
        Bus.observe<String>(Constants.LOGIN_SUCCESS, this) {
            collect_tv_nickname.text = it
            collect_iv_avatar.loadImg(Constants.TEMP_AVATAR, error = R.drawable.un_login)
        }
    }

    override fun initAction() {
        super.initAction()
        collect_tv_local_song.fastClickListener {
            NavUtil.nav(it,R.id.to_local_song_fragment)
        }
        collect_tv_recent_read.fastClickListener {
            NavUtil.nav(it,R.id.to_recent_read_fragment)
        }
        collect_tv_recent_listen.fastClickListener {
            NavUtil.nav(it,R.id.to_recent_listen_fragment)
        }
        collect_tv_downloaded.fastClickListener {
            NavUtil.nav(it,R.id.to_downloaded_fragment)
        }
        collect_fl_loved_song.fastClickListener {
            NavUtil.nav(it,R.id.to_loved_song_fragment)
        }
        collect_fl_loved_poetry.fastClickListener {
            NavUtil.nav(it,R.id.to_loved_poetry_fragment)
        }
        collect_layout_to_login.fastClickListener {
            if (isLogin) {
                NavUtil.nav(it,R.id.to_mime_fragment)
            } else {
                NavUtil.toLogin(requireActivity())
            }
        }
        collect_iv_create_new_song_list.fastClickListener {
            if (createNewSongListDialog == null) {
                createNewSongListDialog =
                    CreateNewSongListDialog(requireContext(), object : OnDialogBtnClickListener {
                        override fun onBtnClick(text: String) {
                            LogUtil.e("tetx:$text")
                            val songListId = randomOnlySongListId()
                            val cover = SongListCover(
                                listName = text,
                                type = songListId,
                                isUserCreated = 1
                            )
                            mViewModel.addAndFindThisSongList(cover)
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