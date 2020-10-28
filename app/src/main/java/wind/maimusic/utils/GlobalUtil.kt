package wind.maimusic.utils

import kotlinx.coroutines.*

/**
 * 使用此工具类对数据库进行查询
 * 可以将T换成对应的song类型
 */
object GlobalUtil {
    fun <T> execute(block:suspend () -> T) :T{
        return runBlocking {
            withContext(Dispatchers.IO) {
                block.invoke()
            }
        }
    }
    fun <T> async(block:suspend () -> T) :T?{
        var result:T?=null
        GlobalScope.launch {
            result = withContext(Dispatchers.IO) {
                block.invoke()
            }
        }
        return result
    }

}