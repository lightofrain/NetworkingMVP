package com.suhang.networkmvp.function;

import com.suhang.networkmvp.mvp.event.BaseEvent;
import com.suhang.networkmvp.mvp.result.BaseResult;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by 苏杭 on 2017/5/2 11:18.
 */
public class SubstribeManager {
    @Inject
    RxBus mRxBus;
    @Inject
    CompositeDisposable mDisposable;

    @Inject
    public SubstribeManager() {
    }

    /**
     * 发送事件
     */
    public void post(Object o) {
        mRxBus.post(o);
    }

    /**
     * 订阅成功事件(订阅后才可收到该事件,订阅要在获取数据之前进行)
     *
     * @param aClass 继承BaseResult的结果类的字节码
     */
    public <V extends BaseResult> FlowableWrap<V> subscribeResult(Class<V> aClass) {
        return new FlowableWrap<>(mRxBus.toFlowable(aClass).observeOn(AndroidSchedulers.mainThread()).onBackpressureDrop(), mDisposable);
    }

    /**
     *
     *
     */
    public <T extends BaseEvent> FlowableWrap<T> subscribeEvent(Class<T> aClass) {
        return new FlowableWrap<>(mRxBus.toFlowable(aClass).observeOn(AndroidSchedulers.mainThread()).onBackpressureDrop(), mDisposable);
    }

}