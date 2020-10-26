package wind.widget.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import wind.widget.R
import wind.widget.cost.Consts
import wind.widget.model.Song
import java.io.*
import kotlin.concurrent.thread

/**
 * @By Journey 2020/10/26
 * @Description
 */
object SongUtil {
    /**
     * 将一个对象写入流中 对象必须实现Serializable接口
     * 将当前的歌曲song对象存入本地
     */
    fun saveSong(context: Context,song: Song?) {
        try {
            if (song != null) {
                val file =
                    File(Consts.currentSongUrl(context))
                if (!file.exists()) {
                    file.mkdirs()
                }
                //写对象流的对象
                val songFile = File(file, "song.txt")
                val oos =
                    ObjectOutputStream(FileOutputStream(songFile))
                oos.writeObject(song) //将Person对象p写入到oos中
                oos.close() //关闭文件流
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("JG","写入对象error!")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("JG","写入对象error!")
        }
    }

    fun getSong(context: Context): Song? {
        try {
            val input = FileInputStream("${Consts.currentSongUrl(context)}/song.txt")
            val ois = ObjectInputStream(input)
            ois.close()//todo
            return ois.readObject() as Song //返回对象
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("JG","读取对象error!")
            return null
        } catch (e: IOException) {
            Log.e("JG","读取对象error!")
            e.printStackTrace()
            return null
        } catch (e: ClassNotFoundException) {
            Log.e("JG","读取对象error!")
            e.printStackTrace()
            return null
        }
    }

    /**
     * 将本地音乐的封面图片存入本地
     */
    fun saveSongCover(context: Context,bitmap: Bitmap?, singer:String):Boolean {
        if (bitmap != null) {
            val file = File(Consts.coverImgUrl(context))
            if (!file.exists()) {
                file.mkdirs()
            }
            val singerImgFile = File(file,"$singer.jpg")
            var fos : FileOutputStream?=null
            try {
                fos = FileOutputStream(singerImgFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
                fos.flush()
                return true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return false
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } finally {
                try {
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }

    /**
     * 读取本地音乐的封面图片
     */
    fun loadLocalSongCover(context: Context, singer: String, v: ImageView) {
        val mS:String
        mS = if (singer.contains("/")) {
            val s = singer.split("/")
            s[0].trim()
        } else {
            singer.trim()
        }
        val imgUrl = "${Consts.coverImgUrl(context)}$mS.jpg"
//        LogUtil.e("本地音乐封面路径：$imgUrl")
        v.loadImg(imgUrl,placeholder = R.drawable.disk,error = R.drawable.disk)
    }

    /**
     * 保存歌词到本地
     */
    fun saveLrcText(context: Context,lrc:String,songName:String) {
        thread {
            val file = File(Consts.lrcTextUrl(context))
            if (!file.exists()) {
                file.mkdirs()
            }
            val name = if (songName.isBlank()) {
                "maimusic"
            } else {
                songName
            }
            val lrcFile = File(file,"$name.lrc")
            try {
                val fileWriter = FileWriter(lrcFile)
                fileWriter.write(lrc)
                fileWriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 读取本地歌词
     */
    fun loadLrcText(context: Context,songName:String) :String? {
        return try {
            val fileReader = FileReader("${Consts.lrcTextUrl(context)}$songName.lrc")
            val bufferReader = BufferedReader(fileReader)
            val lrc = StringBuilder()
            while(true) {
                val s = bufferReader.readLine() ?: break
                lrc.append(s).append("\n")
            }
            fileReader.close()
//            LogUtil.e("本地歌词：$lrc")
            lrc.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}