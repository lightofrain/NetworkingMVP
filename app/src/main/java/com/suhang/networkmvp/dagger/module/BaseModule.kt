package com.suhang.networkmvp.dagger.module

import android.app.Activity
import android.content.Context
import com.jakewharton.disklrucache.DiskLruCache
import com.suhang.networkmvp.annotation.BaseScope
import com.suhang.networkmvp.application.BaseApp
import com.suhang.networkmvp.utils.SystemUtil
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.io.File

/**
 * Created by 苏杭 on 2017/1/20 16:26.
 * 用于提供公共对象(公用但非单例)
 */

abstract class BaseModule(private val mActivity: Activity) {
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    @BaseScope
    @Provides
    fun provideActivity(): Activity {
        return mActivity
    }

    @BaseScope
    @Provides
    fun provideContext(): Context {
        return mActivity
    }

    @Provides
    @BaseScope
    fun provideCD(): CompositeDisposable {
        return mCompositeDisposable
    }


    /**
     * 提供硬盘缓存工具
     * @return
     */
    @Provides
    @BaseScope
    fun provideDiskLruCache(): DiskLruCache {
        val diskLruCache: DiskLruCache = DiskLruCache.open(File(BaseApp.CACHE_PATH), SystemUtil.getAppVersion(), 1, (1024 * 1024 * 100).toLong())
        return diskLruCache
    }
}
