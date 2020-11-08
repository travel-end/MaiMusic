package wind.maimusic.ui.fragment.singer

import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.singer.Singer
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.vm.SingerViewModel
import wind.widget.effcientrv.addItem
import wind.widget.effcientrv.setText
import wind.widget.effcientrv.setup
import wind.widget.effcientrv.submitList
import wind.widget.utils.loadImg

class SingerFragment:BaseLifeCycleFragment<SingerViewModel>() {
    private lateinit var rvRecommendSingers:RecyclerView
    private lateinit var rvAllSingers:RecyclerView

    override fun layoutResId()=R.layout.fragment_singers
    override fun initView() {
        super.initView()
        rvRecommendSingers = mRootView.findViewById(R.id.singer_rv_recommend)
        rvAllSingers = mRootView.findViewById(R.id.singer_rv_all)
        mRootView.findViewById<TextView>(R.id.title_tv).text = "歌手"
        val glm = GridLayoutManager(requireContext(),3)
        rvRecommendSingers.setup<Singer> {
            withLayoutManager {
                return@withLayoutManager glm
            }
            adapter {
                addItem(R.layout.item_single_singer) {
                    bindViewHolder { data, position, holder ->
                        data?.let {
                            val ivCover = itemView?.findViewById<ShapeableImageView>(R.id.item_single_singer_cover)
                            ivCover?.shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                            ivCover?.loadImg(it.cover?:"")
                            setText(R.id.item_single_singer_name,it.name)
                        }
                    }
                }
            }
        }
        rvAllSingers.setup<Singer> {
            adapter {
                addItem(R.layout.item_single_singer_horizontal) {
                    bindViewHolder { data, position, holder ->
                        data?.let {
                            val ivCover = itemView?.findViewById<ShapeableImageView>(R.id.item_hori_singer_cover)
                            ivCover?.shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                            ivCover?.loadImg(it.cover?:"")
                            setText(R.id.item_hori_singer_name,it.name)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.initRecommendSingers()
        mViewModel.findAllSingers()
    }

    override fun observe() {
        super.observe()
        mViewModel.mRecommendSingers.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                rvRecommendSingers.submitList(it.toMutableList())
            }
        })
        mViewModel.allSingers.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                rvAllSingers.submitList(it.toMutableList())
            }
        })
    }
}