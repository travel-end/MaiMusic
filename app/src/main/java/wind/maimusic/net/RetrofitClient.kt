package wind.maimusic.net

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import wind.maimusic.Constants
import wind.maimusic.utils.LogUtil
import java.util.concurrent.TimeUnit


/**
 * @By Journey 2020/9/26
 * @Description
 */
class RetrofitClient {
    companion object {
        val instance by lazy {
            RetrofitClient()
        }
    }
    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(80, TimeUnit.SECONDS)
        .writeTimeout(80, TimeUnit.SECONDS)
        .addNetworkInterceptor(getLoggerInterceptor())
        .build()

    private fun getLoggerInterceptor() :HttpLoggingInterceptor{
        val httpLoggingInterceptor = HttpLoggingInterceptor(ILoggerInterceptor())
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }


    class ILoggerInterceptor: HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            LogUtil.e(message)
        }
    }


    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constants.FIDDLER_BASE_QQ_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    // 获取歌手照片
    private val singerRetrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constants.SINGER_PIC_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    // 得到播放地址
    private val songUrlRetrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constants.FIDDLER_BASE_SONG_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    val singerApiService: ApiService = singerRetrofit.create(ApiService::class.java)

    val songUrlApiService: ApiService = songUrlRetrofit.create(ApiService::class.java)

}