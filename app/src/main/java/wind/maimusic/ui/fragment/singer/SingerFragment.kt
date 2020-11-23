package wind.maimusic.ui.fragment.singer

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.singer.AllSingers
import wind.maimusic.model.singer.RecommendSingers
import wind.maimusic.model.singer.SingerSexClassify
import wind.maimusic.utils.*
import wind.maimusic.vm.SingerViewModel
import wind.widget.effcientrv.*
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg
import kotlin.math.sin

/**
 * 全部歌手
 */
class SingerFragment:BaseLifeCycleFragment<SingerViewModel>() {
    private lateinit var rvAllSingers:RecyclerView
    private var classifyAction:Int = Constants.MALE
    override fun layoutResId()=R.layout.fragment_singers
    override fun initView() {
        super.initView()
        rvAllSingers = mRootView.findViewById(R.id.singer_rv_list)
        mRootView.findViewById<TextView>(R.id.title_tv).text = R.string.singer.getStringRes()
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
//                                        LogUtil.e("----SingerFragment singerName:${singer.name}")
                                        NavUtil.singerToSongList(it,singer)
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
                        setText(R.id.item_singer_hot_singer,item.hotSinger)
                        setText(R.id.item_singer_other_singer,item.otherSinger)
                        itemView?.let {
                            it.findViewById<TextView>(R.id.item_singer_tv_male).fastClickListener {
                                if (classifyAction != Constants.MALE) {
                                    mViewModel.findSingerByClassify(Constants.MALE)
                                    classifyAction = Constants.MALE
                                }
                            }
                            it.findViewById<TextView>(R.id.item_singer_tv_famale).fastClickListener {
                                if (classifyAction != Constants.FAMELE) {
                                    mViewModel.findSingerByClassify(Constants.FAMELE)
                                    classifyAction = Constants.FAMELE
                                }
                            }
                        }
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
                                        NavUtil.singerToSongList(mRootView, singer)
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
        requireLazyInit()
    }

    override fun lazyInitData() {
        super.lazyInitData()
        mViewModel.initSingersData()
    }

    override fun observe() {
        super.observe()
        mViewModel.singerData.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                rvAllSingers.submitList(it)
            }
        })
        mViewModel.classifySinger.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                rvAllSingers.updateData(2,AllSingers(it))
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