package com.suhang.networkmvp.ui.pager

import android.app.Activity
import com.suhang.networkmvp.R
import com.suhang.networkmvp.annotation.PagerScope
import com.suhang.networkmvp.dagger.module.BlankModule
import com.suhang.networkmvp.domain.AppMain
import com.suhang.networkmvp.domain.GithubBean
import com.suhang.networkmvp.mvp.model.AttentionModel
import com.suhang.networkmvp.mvp.result.SuccessResult
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.pager_attention_one.view.*
import org.jetbrains.anko.info

/**
 * Created by 苏杭 on 2017/1/24 16:28.
 */
@PagerScope
class AttentionOnePager(activity: Activity) : BasePager<AttentionModel>(activity) {
    override fun bindLayout(): Int {
        return R.layout.pager_attention_one
    }

    override fun subscribeEvent() {
        manager.subscribeResult(SuccessResult::class.java).subscribe(Consumer {
            val result = it.result
            when (result) {
                is AppMain->{
                    info(result)
                }

                is GithubBean->{
                    info(result)
                }
            }
        })

    }

    override fun initEvent() {
        root.button.setOnClickListener {
            model.getAppMainData()
        }
        root.button1.setOnClickListener {
            model.getGithubData()
        }
    }


    override fun injectDagger() {
        baseComponent.providerBlankComponent(BlankModule()).inject(this)
    }

    override fun initData() {
    }

}