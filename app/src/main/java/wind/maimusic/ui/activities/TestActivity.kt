package wind.maimusic.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.model.Image
import wind.maimusic.model.searchhot.HistoryTag
import wind.maimusic.model.title.HotSearchTitle
import wind.maimusic.model.title.SingleSongTitle
import wind.maimusic.model.title.Title
import wind.maimusic.utils.inflate
import wind.maimusic.utils.toast
import wind.maimusic.widget.MaiRefreshView
import wind.widget.effcientrv.*
import wind.widget.jrecyclerview.JRecycleView
import wind.widget.jrecyclerview.adapter.JRefreshAndLoadMoreAdapter
import wind.widget.jrecyclerview.config.JRecycleConfig

/**
 * @By Journey 2020/11/4
 * @Description
 */
class TestActivity : AppCompatActivity() {
    companion object {
        const val PAGE_SIZE = 20
    }

    val data = mutableListOf<String>()
    val data2 = mutableListOf<Any>()
    lateinit var rv:JRecycleView
    private var jAdapter: JRefreshAndLoadMoreAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        rv = findViewById<JRecycleView>(R.id.jrv)
//        val lm = GridLayoutManager(this,3)
//        rv.layoutManager = lm

//        initData()

//        val rawAdapter = object : RecyclerView.Adapter<RawViewHolder>() {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RawViewHolder {
//                return RawViewHolder(R.layout.item_test.inflate(parent))
//            }
//
//            override fun getItemCount() = data.size
//            override fun onBindViewHolder(holder: RawViewHolder, position: Int) {
//                holder.tvContent.text = data[position]
//                holder.tvContent.setOnClickListener {
//                    "${data[position]}".toast()
//                }
//            }
//        }
        initData2()

        impl2()

        jAdapter = JRefreshAndLoadMoreAdapter(this, adapter)
        /*加载更多*/
//        jAdapter?.setOnLoadMoreListener {
//            if (data.size > 2 * PAGE_SIZE) {
//                jAdapter?.setLoadError()
//            } else {
//                val size = data.size
//                addData()
//                jAdapter?.setLoadComplete()
//                jAdapter?.notifyItemRangeChanged(jAdapter?.getRealPosition(size)!!, PAGE_SIZE)
//            }
//        }
//        /*下拉刷新*/
//        jAdapter?.setOnRefreshListener {
//            Handler(Looper.getMainLooper()).postDelayed({
//                getRefreshData()
//                jAdapter?.resetLoadMore()
//                jAdapter?.setRefreshComplete()
//            },2000)
//        }
        jAdapter?.refreshLoadView = MaiRefreshView(baseContext)
        rv.adapter = jAdapter

        adapter?.submitList(data2)
    }

    fun initData() {
        data.clear()
        for (i in 0 until PAGE_SIZE) {
            data.add("初始数据recyclerView${i + 1}")
        }
    }

    fun addData() {
        val size = data.size
        for (i in 1..20) {
            data.add("${(size + i)}上啦加载的recyclerView")
        }
    }
    fun getRefreshData() {
        data.clear()
        for (i in 0..15) {
            data.add("这是刷新的数据$i")
        }
    }

    inner class RawViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContent: TextView = itemView.findViewById(R.id.tv_content)
    }

    private fun initData2() {
        data2.add(Title(title = "My Friends"))//
        data2.add(HotSearchTitle(title = "Jack", icon = R.drawable.temp_icon,text =  "123456789XX"))
        data2.add(HotSearchTitle(title = "Jack", icon = R.drawable.temp_icon,text =  "123456789XX"))
        data2.add(Title(title = "My Images"))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Image(R.drawable.temp_logo))
        data2.add(Title("My Musics"))
        data2.add(HistoryTag(name = "Love story",id =  R.drawable.temp_icon))
        data2.add(HistoryTag(name = "Love story",id =  R.drawable.temp_icon))
        data2.add(HistoryTag(name = "Love story",id =  R.drawable.temp_icon))//pos:17
    }

    var adapter: EfficientAdapter<Any>? = null
    private fun impl2() {
        val gridLayoutManager = GridLayoutManager(this@TestActivity, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
//                if (jAdapter?.getIt)
                Log.e("JG","---position:$position")

                return if (adapter?.getItem(position-1) is Image) 1 else 3
            }
        }
        rv.layoutManager = gridLayoutManager
        adapter = efficientAdapter<Any> {
            /*put viewType and holder*/
            addItem(R.layout.item_test) {
                isForViewType { data, _ -> data is Title }
                bindViewHolder { data, _, _ ->
                    val header = data as Title
                    setText(R.id.tv_content, header.title)
                }
            }
            addItem(R.layout.item_single_song) {
                isForViewType { data, _ -> data is HotSearchTitle }
                bindViewHolder { data, _, _ ->
                    val user = data as HotSearchTitle
                    setText(R.id.single_song_tv_name, user.title)
                    setImageResource(R.id.single_song_iv_cover, user.icon!!)
                    //如果你的控件找不到方便赋值的方法，可以通过 findViewById 去查找
                    val phone = findViewById<TextView>(R.id.single_song_tv_desc)
                    phone.text = user.text
                }
            }
            addItem(R.layout.item_song_list_cover) {
                isForViewType { data, _ -> data is Image }
                bindViewHolder { data, _, _ ->
                    val image = data as Image
                    setImageResource(R.id.item_song_list_iv_cover, image.icon)
                }
            }
            addItem(R.layout.item_poetry_song) {
                isForViewType { data, _ -> data is HistoryTag }
                bindViewHolder { data, _, _ ->
                    val music = data as HistoryTag
                    setText(R.id.find_tv_sp_author, music.name)
                    setImageResource(R.id.find_sp_iv_icon, music.id!!)
                }
            }
        }
//            .attach(rv)
    }

    fun testList() {
        val list = mutableListOf<String>()
        for (i in 0..10) {
            list.add("zz$i")
        }
    }
}