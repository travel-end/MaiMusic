package wind.maimusic.utils

import kotlinx.coroutines.*

/**
 * 使用此工具类对数据库进行查询
 * 可以将T换成对应的song类型
 */
typealias Block<T> = suspend () -> T
object GlobalUtil {
    fun <T> execute(block:Block<T>) :T{
        return runBlocking {
            withContext(Dispatchers.IO) {
                block.invoke()
            }
        }
    }
    fun <T> async(block:Block<T>) :T?{
        var result:T?=null
        GlobalScope.launch {
            result = withContext(Dispatchers.IO) {
                block.invoke()
            }
        }
        return result
    }
}