package com.suhang.networkmvp.ui.fragment

import android.support.v7.widget.LinearLayoutManager

import com.suhang.networkmvp.R
import com.suhang.networkmvp.adapter.HomeRvAdapter
import com.suhang.networkmvp.annotation.FragmentScope
import com.suhang.networkmvp.binding.data.BaseData
import com.suhang.networkmvp.dagger.module.BlankModule
import com.suhang.networkmvp.databinding.FragmentHomeBinding
import com.suhang.networkmvp.domain.DeleteHistoryBean
import com.suhang.networkmvp.domain.HomeBean
import com.suhang.networkmvp.mvp.event.BindingEvent
import com.suhang.networkmvp.mvp.model.HomeModel
import com.suhang.networkmvp.mvp.result.ErrorResult
import com.suhang.networkmvp.mvp.result.SuccessResult
import com.suhang.networkmvp.utils.LogUtil

import java.util.ArrayList

import javax.inject.Inject

/**
 * Created by 苏杭 on 2017/1/24 15:31.
 */
@FragmentScope
class HomeFragment : BaseFragment<HomeModel, FragmentHomeBinding>() {
    @Inject
    lateinit var mAdapter: HomeRvAdapter
    private val mLs1 = ArrayList<Int>()
    private val mLs2 = ArrayList<String>()

    override fun bindLayout(): Int {
        return R.layout.fragment_home
    }

    override fun subscribeEvent() {
        //        getSm().subscribeEvent(BindingEvent.class).subscribe(bindingEvent -> {
        //            switch (bindingEvent.getId()) {
        //                case R.id.button:
        //                    getModel().getLoadMore(mAdapter.getNextPage());
        //                    break;
        //                case R.id.parent:
        //                    LogUtil.i("啊啊啊"+ bindingEvent.getData());
        //                    break;
        //            }
        //        });
        //        getSm().subscribeResult(ErrorResult.class).subscribe(errorResult -> {
        //            LogUtil.i("啊啊啊" + errorResult.getResult());
        //        });
        //        getSm().subscribeResult(SuccessResult.class).subscribe(successResult -> {
        //            if (successResult.getTag() == TAG_LOADMORE) {
        //                HomeBean result = successResult.getResult(HomeBean.class);
        //                mAdapter.setTotalCount(Integer.parseInt(result.getTotal()));
        //                mAdapter.loadMore(result.getList());
        //            } else if (successResult.getTag() == TAG) {
        //                HomeBean result = successResult.getResult(HomeBean.class);
        //                mAdapter.setTotalCount(Integer.parseInt(result.getTotal()));
        //                mAdapter.notifyDataSetChanged(result.getList());
        //            } else if (successResult.getTag() == TAG_DELETE) {
        //                DeleteHistoryBean result = successResult.getResult(DeleteHistoryBean.class);
        //                if (result.getFailedList() != null && "".equals(result.getFailedList())) {
        //                    getModel().getHomeData(mAdapter.getCurrentPage() * mAdapter.getPageSize(), (List<Integer>) result.getAppendMessage());
        //                }
        //            } else if (successResult.getTag() == TAG_LOADMORE_NORMAL) {
        //                HomeBean result = successResult.getResult(HomeBean.class);
        //                mAdapter.setTotalCount(Integer.parseInt(result.getTotal()));
        //                mAdapter.notifyDelete((List<Integer>) result.getAppendMessage(), result.getList());
        //                mLs1.clear();
        //                mLs2.clear();
        //            }
        //
        //        });

    }

    override fun initData() {
        mBinding.rvHome.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.rvHome.adapter = mAdapter
        model.getHomeData()
    }

    override fun injectDagger() {
        baseComponent.providerBlankComponent(BlankModule()).inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter!!.destory()
    }

    companion object {
        val TAG = 100
        val TAG_LOADMORE = 101
        val TAG_DELETE = 102
        val TAG_LOADMORE_NORMAL = 103
    }
}
