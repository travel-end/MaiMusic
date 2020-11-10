package wind.maimusic.ui.fragment.singer

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import okhttp3.internal.wait
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.singer.AllSingers
import wind.maimusic.model.singer.RecommendSingers
import wind.maimusic.model.singer.Singer
import wind.maimusic.model.singer.SingerSexClassify
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.inflate
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.visible
import wind.maimusic.vm.SingerViewModel
import wind.widget.effcientrv.addItem
import wind.widget.effcientrv.setText
import wind.widget.effcientrv.setup
import wind.widget.effcientrv.submitList
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg

/**
 * 歌手
 */
class SingerFragment:BaseLifeCycleFragment<SingerViewModel>() {
    private lateinit var rvAllSingers:RecyclerView
    override fun layoutResId()=R.layout.fragment_singers
    override fun initView() {
        super.initView()
        rvAllSingers = mRootView.findViewById(R.id.singer_rv_list)
        mRootView.findViewById<TextView>(R.id.title_tv).text = "歌手"
        val glm = GridLayoutManager(requireContext(),3)
        rvAllSingers.setup<Any> {
            adapter {
                addItem(R.layout.item_singer_recommend) {
                    isForViewType { data, position -> data is RecommendSingers }
                    bindViewHolder { data, position, holder ->
                        val itemList = (data as RecommendSingers).recomSingers
                        val rvRecommend:RecyclerView? = itemView?.findViewById(R.id.singer_rv_recommend)
                        rvRecommend?.let {rv->
                            rv.layoutManager = glm
                            rv.adapter = object :RecyclerView.Adapter<RecommendSingerViewHolder>() {
                                override fun onCreateViewHolder(
                                    parent: ViewGroup,
                                    viewType: Int
                                ): RecommendSingerViewHolder {
                                    return RecommendSingerViewHolder(R.layout.item_single_singer.inflate(parent))
                                }

                                override fun getItemCount()=itemList.size

                                override fun onBindViewHolder(
                                    holder: RecommendSingerViewHolder,
                                    position: Int
                                ) {
                                    val singer = itemList[position]
                                    holder.ivSingerCover.shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                                    holder.ivSingerCover.loadImg(singer.cover?:"")
                                    holder.tvSingerName.text = singer.name
                                    holder.itemView.fastClickListener {
                                        LogUtil.e("----SingerFragment singerName:${singer.name}")
                                    }
                                }
                            }
                        }
                    }
                }
                addItem(R.layout.item_singer_sex_classify) {
                    isForViewType { data, position -> data is SingerSexClassify }
                    bindViewHolder { data, position, holder ->
                        val item = data  as SingerSexClassify
                        setText(R.id.item_singer_tv_male,item.maleSinger)
                        setText(R.id.item_singer_tv_famale,item.famaleSinger)
                    }
                }
                addItem(R.layout.item_vercital_rv) {
                    isForViewType { data, position -> data is AllSingers }
                    bindViewHolder { data, position, holder ->
                        val itemList = (data as AllSingers).allSingers
                        val rvAllSinger:RecyclerView? = itemView?.findViewById(R.id.item_ver_rv)
                        rvAllSinger?.let {rv->
                            rv.adapter = object :RecyclerView.Adapter<AllSingerViewHolder>() {
                                override fun onCreateViewHolder(
                                    parent: ViewGroup,
                                    viewType: Int
                                ): AllSingerViewHolder {
                                    return AllSingerViewHolder(R.layout.item_single_singer_horizontal.inflate(parent))
                                }
                                override fun getItemCount()=itemList.size
                                override fun onBindViewHolder(
                                    holder: AllSingerViewHolder,
                                    position: Int
                                ) {
                                    val singer = itemList[position]
                                    holder.ivSingerCover.shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                                    holder.ivSingerCover.loadImg(singer.cover?:"")
                                    holder.tvSingerName.text = singer.name
                                    holder.itemView.fastClickListener {
                                        LogUtil.e("----SingerFragment singerName:${singer.name}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        handler.sendEmptyMessageDelayed(1,220)
    }
    private val handler=object :Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what==1) {
                mViewModel.initSingersData()
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.singerData.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                rvAllSingers.submitList(it)
            }
        })
    }

    class RecommendSingerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ivSingerCover:ShapeableImageView = itemView.findViewById(R.id.item_single_singer_cover)
        val tvSingerName:TextView= itemView.findViewById(R.id.item_single_singer_name)
    }
    class AllSingerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ivSingerCover:ShapeableImageView = itemView.findViewById(R.id.item_hori_singer_cover)
        val tvSingerName:TextView = itemView.findViewById(R.id.item_hori_singer_name)
    }
}