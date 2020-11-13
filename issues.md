####问题
1、参考playAndroid 项目的room数据库表达方式 以及build中的压缩apk方式
2、对于本项目中的tablayout，考虑使用android自带的tablayout，因为用的东西比较简单，使用的commonTabLayout太重了
3、考虑使用原生的statusBar，不适用ImmesionBar
4、写入本地的song.txt应该加密，其他的文件也是
5、为保证用户下载的音乐在卸载app后仍然能够使用，建议使用`共享储存空间`  -->`https://developer.android.com/training/data-storage/app-specific#external`
6、fragment与viewPager的懒加载和fragment的show、hide问题、fragment之间通信问题
7、菜鸟窝pagerRecyclerView，处理搜索页的分页问题
8、使用JRecyclerView处理下拉刷新问题
9、对于播放搜索音乐，下一首播放的内容，需要存储一个本地歌单来播放下一首（这种情况很少，就使用这种方式处理）
10、使用IntentService: 用于处理后台长时间的耗时操作，如：下载文件、播放音乐（使用JobIntentService代替DownloadService(Service)的下载服务，IntentService已经过时）
11、部分页面（playActivity）参考ios 的ui，ios 
12、使用jetPack的WokerManager做定时任务（定时音乐）、包括在application中使用Woker类读取json文件
13、关于jetPack库使用:https://github.com/OnexZgj/Jetpack_Component
14、能不能把数据存进数据库之后，把数据库导出，把assets文件清空。最后打包的时候，把确定的数据库在导入到最终的包中(为了省出assets占的空间)
15、改造IndicatorView创建本项目首页的tabLayout指示器(或者参考MagicIndicator)
16、对于已经下载的音乐的处理（包括本地音乐和已经下载的音乐，都算isDownload，这两个音乐是存在不同的路径下面的）

####todo
1、关于分区存储
https://www.bilibili.com/video/BV1fT4y1g74Z


indicatorView
                .setSliderColor(getResColor(R.color.teal_200), getResColor(R.color.red_checked_color))
                .setSliderWidth(resources.getDimension(R.dimen.dp_17))
                .setSliderHeight(resources.getDimension(R.dimen.dp_5))
                .setSlideMode(IndicatorSlideMode.WORM)
                .setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                .setupWithViewPager(viewPager2)
                
遇到的坑：
1、NestedScrollView嵌套RecyclerView，如果RecyclerView的item项太多，会导致NestedScrollView的测量花费很长的时间，导致页面刷新缓慢        



####github
https://github.com/smashinggit/Study



####学习记录
1、关于viewPager2
viewPager2消除了viewPager需要处理的懒加载机制
                
