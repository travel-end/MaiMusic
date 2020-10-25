package wind.maimusic.utils

import java.lang.reflect.ParameterizedType

/**
 * @By Journey 2020/10/25
 * @Description
 */

/**
 * 通过反射获取父类泛型<T>对应的Class类
 */
@Suppress("UNCHECKED_CAST")
fun <T> getClass(t:Any):Class<T> = (t.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>