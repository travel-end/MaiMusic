//package wind.maimusic.utils.cache
//
//import com.danikula.videocache.file.Md5FileNameGenerator
//
///**
// * @By Journey 2020/11/24
// * @Description 处理相同歌曲不同的播放的地址 生成同一个缓存文件名
// */
//class CacheFileNameGenerator:Md5FileNameGenerator() {
//    override fun generate(url: String?): String {
//        url?.let {
//            val len = it.split("/").size
//            val newUrl = it.split("/")[len-1].replace(".mp3","")
//            val newUrl1 = newUrl.split("\\?")[0]
//
//        }
//        return super.generate(url)
//    }
//}