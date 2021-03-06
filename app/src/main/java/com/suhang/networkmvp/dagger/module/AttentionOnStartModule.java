package com.suhang.networkmvp.dagger.module;


import com.suhang.networkmvp.annotation.PagerScope;
import com.suhang.networkmvp.mvp.contract.IAttentionContract;

import dagger.Module;

/**
 * Created by 苏杭 on 2017/1/20 17:03.
 */

@PagerScope
@Module
public class AttentionOnStartModule extends NetworkModule<IAttentionContract.IAttentionView>{
    public AttentionOnStartModule(IAttentionContract.IAttentionView view) {
        super(view);
    }
}
