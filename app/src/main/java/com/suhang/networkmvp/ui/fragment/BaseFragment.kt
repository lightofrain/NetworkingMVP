package com.suhang.networkmvp.ui.fragment

import android.app.Activity
import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import com.suhang.layoutfinder.ContextProvider
import com.suhang.layoutfinder.LayoutFinder
import com.suhang.layoutfinderannotation.BindLayout
import com.suhang.networkmvp.application.BaseApp
import com.suhang.networkmvp.dagger.component.BaseComponent
import com.suhang.networkmvp.dagger.module.BaseModule
import com.suhang.networkmvp.function.SubstribeManager
import com.suhang.networkmvp.mvp.model.BaseModel
import com.suhang.networkmvp.utils.ScreenUtils

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import javax.inject.Inject

import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger

/**
 * Created by 苏杭 on 2017/1/21 10:52.
 */
abstract class BaseFragment<T : BaseModel, E : ViewDataBinding> : Fragment(), ContextProvider ,AnkoLogger{

    /**
     * 基主件,用于注册子主件(dagger2)
     */
    /**
     * 获取父Component(dagger2)
     */
    lateinit var baseComponent: BaseComponent

    //Rxjava事件集合，用于退出时取消事件
    @Inject
    lateinit var mDisposables: CompositeDisposable

    @Inject
    lateinit var mActivity: Activity

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var model: T

    @BindLayout
    lateinit var mBinding: E

    /**
     * 获取RxBus,可进行订阅操作
     */
    @Inject
    lateinit var sm: SubstribeManager

    //fragment布局缓存
    protected var cacheView: View? = null
    //是否为缓存布局
    protected var isCacheView: Boolean = false

    private var isRegisterEventBus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseComponent = (activity.application as BaseApp).appComponent.baseComponent(BaseModule(activity))
        injectDagger()
        subscribeEvent()
        bind(bindLayout())
    }

    private fun bind(layout: Int) {
        LayoutFinder.find(this, layout)
    }

    /**
     * 绑定布局

     * @return
     */
    protected abstract fun bindLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        onViewCreate(inflater, container, savedInstanceState)
        return rootView
    }

    /**
     * 需要在绑定布局之前(onCreateView之前)做处理则覆盖此方法

     * @param inflater
     * *
     * @param container
     * *
     * @param savedInstanceState
     */
    protected fun onViewCreate(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {}

    /**
     * 缓存Fragment的布局
     * 获得布局View,仅在onCreateView()方法中使用
     */
    protected val rootView: View?
        get() {
            if (cacheView == null) {
                cacheView = mBinding.root
            } else {
                isCacheView = true
                val parent = cacheView!!.parent as ViewGroup
                parent.removeView(cacheView)
            }
            return cacheView
        }

    /**
     * 订阅事件
     */
    protected abstract fun subscribeEvent()

    /**
     * 注册事件总线
     */
    protected fun registerEventBus() {
        EventBus.getDefault().register(this)
        isRegisterEventBus = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initEvent()
    }


    /**
     * EventBus事件(防崩溃,需要则重写)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(i: Int?) {

    }

    /**
     * 初始化数据
     */
    protected abstract fun initData()


    /**
     * 初始化事件
     */
    protected fun initEvent() {

    }

    /**
     * dagger2绑定(需要则重写) ps: getBaseComponent().getMainComponent(new
     * MainModule()).inject(this);
     */
    protected abstract fun injectDagger()


    /**
     * 隐藏软键盘
     */
    protected fun hideKeyboard() {
        val view = activity.currentFocus
        if (view != null) {
            (activity.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 沉浸状态栏偏移
     */
    protected fun immerseUI(view: View) {
        view.setPadding(0, ScreenUtils.getStatusBarHeight(context), 0, 0)
    }


    /**
     * 显示软键盘
     */
    protected fun showKeyboard(et: EditText) {
        et.requestFocus()
        (activity.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
        model.destory()
        EventBus.getDefault().unregister(this)
    }


    /**
     * 有时会有Activity给关闭而内部Fragment不走onDestory()方法,则可手动调用此方法销毁数据
     */
    fun destory() {
        mDisposables.dispose()
        //取消所有正在进行的网络任务
        if (isRegisterEventBus) {
            EventBus.getDefault().unregister(this)
        }
        model.destory()
    }

    override fun providerContext(): Context {
        return context
    }

    companion object {
        //基类内部错误tag
        val ERROR_TAG = -1
    }
}
