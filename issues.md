1、参考playAndroid 项目的room数据库表达方式 以及build中的压缩apk方式
2、对于本项目中的tablayout，考虑使用android自带的tablayout，因为用的东西比较简单，使用的commonTabLayout太重了
3、考虑使用原生的statusBar，不适用ImmesionBar
4、写入本地的song.txt应该加密，其他的文件也是
5、为保证用户下载的音乐在卸载app后仍然能够使用，建议使用`共享储存空间`  -->`https://developer.android.com/training/data-storage/app-specific#external`
6、fragment与viewPager的懒加载和fragment的show、hide问题、fragment之间通信问题
7、菜鸟窝pagerRecyclerView，处理搜索页的分页问题
8、使用JRecyclerView处理下拉刷新问题




todo
1、关于分区存储
https://www.bilibili.com/video/BV1fT4y1g74Z
