package com.suhang.networkmvp.dagger.component

import com.suhang.networkmvp.annotation.ChildScope
import com.suhang.networkmvp.dagger.module.GrandChildOneModule
import com.suhang.networkmvp.ui.fragment.GrandChildOneFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by 苏杭 on 2017/6/1 20:44.
 */
@ChildScope
@Subcomponent(modules = arrayOf(GrandChildOneModule::class))
interface GrandChildOneComponent :AndroidInjector<GrandChildOneFragment> {
    @Subcomponent.Builder
    abstract class Builder:AndroidInjector.Builder<GrandChildOneFragment>()
}
