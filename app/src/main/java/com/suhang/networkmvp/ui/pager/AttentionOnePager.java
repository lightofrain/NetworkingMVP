package com.suhang.networkmvp.ui.pager;

import android.app.Activity;

import com.suhang.networkmvp.R;
import com.suhang.networkmvp.annotation.PagerScope;
import com.suhang.networkmvp.dagger.module.AttentionOnStartModule;
import com.suhang.networkmvp.databinding.PagerAttentionOneBinding;
import com.suhang.networkmvp.domain.AppMain;
import com.suhang.networkmvp.domain.ErrorBean;
import com.suhang.networkmvp.mvp.contract.IAttentionContract;
import com.suhang.networkmvp.mvp.presenter.AttentionPresenter;


/**
 * Created by 苏杭 on 2017/1/24 16:28.
 */

@PagerScope
public class AttentionOnePager extends BasePager<AttentionPresenter,PagerAttentionOneBinding> implements IAttentionContract.IAttentionView {
    public AttentionOnePager(Activity activity) {
        super(activity);
        bind(R.layout.pager_attention_one);
    }

    @Override
    public void log() {

    }

    @Override
    protected void initEvent() {
        getBinding().data.setOnClickListener(v -> {
            getPresenter().doRefresh();
            getPresenter().doLoadMore();
//            ARouter.getInstance().build(RouterConstants.SPLASH).navigation();
        });
    }

    @Override
    public void setData(ErrorBean e, int tag) {
        AppMain b = (AppMain) e;
        getBinding().data.setText(b.toString());
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected void injectDagger() {
        getBaseComponent().getAttentionOnStartComponent(new AttentionOnStartModule(this)).inject(this);
    }

    @Override
    public void initData() {
        getPresenter().doLog();
    }

    @Override
    public void showError(ErrorBean e, int tag) {

    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }
}
