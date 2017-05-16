package com.suhang.networkmvp.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.suhang.layoutfinder.ContextProvider;
import com.suhang.layoutfinder.LayoutFinder;
import com.suhang.layoutfinderannotation.BindLayout;
import com.suhang.networkmvp.BR;
import com.suhang.networkmvp.application.App;
import com.suhang.networkmvp.binding.event.BaseData;
import com.suhang.networkmvp.constants.ErrorCode;
import com.suhang.networkmvp.dagger.component.BaseComponent;
import com.suhang.networkmvp.dagger.module.BaseModule;
import com.suhang.networkmvp.domain.ErrorBean;
import com.suhang.networkmvp.function.SubstribeManager;
import com.suhang.networkmvp.mvp.model.BaseModel;
import com.suhang.networkmvp.mvp.result.ErrorResult;
import com.suhang.networkmvp.utils.InputLeakUtil;
import com.suhang.networkmvp.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by 苏杭 on 2017/1/21 10:52.
 */
public abstract class BaseActivity<T extends BaseModel, E extends ViewDataBinding> extends AppCompatActivity implements ContextProvider {
	//基类内部错误tag
	public static final int ERROR_TAG = -1;

	/**
	 * 基主件,用于注册子主件(dagger2)
	 */
	private BaseComponent mBaseComponent;

	//Rxjava事件集合，用于退出时取消事件
	@Inject
	CompositeDisposable mDisposables;

	@Inject
	Activity mActivity;

	@Inject
	Context mContext;

	//进度对话框
	@Inject
	Dialog mDialog;

	@Inject
	T mModel;

	@BindLayout
	E mBinding;

	@Inject
	SubstribeManager mManager;

	private boolean isRegisterEventBus;

	@Inject

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBaseComponent = ((App) getApplication()).getAppComponent().baseComponent(new BaseModule(this));
		injectDagger();
		subscribeEvent();
		bind(bindLayout());
		if (mActivity == null) {
			throw new RuntimeException("injectDagger()方法没有实现,或实现不正确");
		}
	}

	/**
	 * 根据提供的布局id进行绑定
	 *
	 * @return 布局id
	 */
	protected abstract int bindLayout();

	protected void bind(@LayoutRes int layout) {
		LayoutFinder.find(this, layout);
		setContentView(mBinding.getRoot());
		setBindingEvent(mBinding);
		initData();
		initEvent();
	}

	/**
	 * 订阅事件
	 */
	protected abstract void subscribeEvent();

	/**
	 * 注册事件总线
	 */
	protected void registerEventBus() {
		EventBus.getDefault().register(this);
		isRegisterEventBus = true;
	}

	public T getModel() {
		return mModel;
	}

	/**
	 * 获取对话框
	 */
	protected Dialog getDialog() {
		return mDialog;
	}


	/**
	 * EventBus事件(防崩溃,需要则重写)
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void event(Integer i) {

	}

	/**
	 * 获取RxBus,可进行订阅操作
	 */
	protected SubstribeManager getSm() {
		return mManager;
	}

	/**
	 * 初始化数据
	 */
	protected abstract void initData();


	/**
	 * 初始化事件
	 */
	protected void initEvent() {

	}

	protected abstract BaseData getBindingData();

	/**
	 * 绑定事件类(暂不使用)
	 */
	protected void setBindingEvent(ViewDataBinding binding) {
		if (getBindingData() != null) {
			getBindingData().setManager(mManager);
			try {
				binding.setVariable(BR.event, getBindingData());
			} catch (Exception e) {
				ErrorBean errorBean = new ErrorBean(ErrorCode.ERROR_CODE_REFLECT_BINDING, ErrorCode.ERROR_DESC_REFLECT_BINDING + "\n" + e.getMessage());
				mManager.post(new ErrorResult(errorBean, ERROR_TAG));
			}
		}
	}

	/**
	 * 获取父Component(dagger2)
	 */
	protected BaseComponent getBaseComponent() {
		return mBaseComponent;
	}

	/**
	 * dagger2绑定(需要则重写) ps: getBaseComponent().getMainComponent(new
	 * MainModule()).inject(this);
	 */
	protected abstract void injectDagger();

	/**
	 * 隐藏软键盘
	 */
	protected void hideKeyboard() {
		View view = getCurrentFocus();
		if (view != null) {
			((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
					hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 沉浸状态栏偏移
	 */
	protected void immerseUI(View view) {
		view.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
	}


	/**
	 * 显示软键盘
	 */
	protected void showKeyboard(EditText et) {
		et.requestFocus();
		((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDisposables != null) {
			mDisposables.dispose();
		}
		if (isRegisterEventBus) {
			EventBus.getDefault().unregister(this);
		}
		//处理InputMethodManager导致的内存泄漏
		mModel.destory();
		InputLeakUtil.fixInputMethodManager(this);
	}

	@Override
	public Context providerContext() {
		return this;
	}
}
