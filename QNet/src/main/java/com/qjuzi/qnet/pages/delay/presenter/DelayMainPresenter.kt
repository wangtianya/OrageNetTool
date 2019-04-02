package com.qjuzi.qnet.pages.delay.presenter

import android.graphics.Color
import com.qjuzi.qnet.R
import com.qjuzi.qnet.manager.StyleManager
import com.qjuzi.qnet.pages.delay.model.DelayPageModel


class DelayMainPresenter(val model: DelayPageModel) {

    fun initData() {
        model.binding.model = model

        StyleManager.getInstance().initStyle(model.context.activity)

        initItemListAndAdapter()
    }



    private fun initItemListAndAdapter() {

        for (i in 1..5) {
            var item = model.DelayItemModel("电信服务器", "www.baidu.com", R.drawable.ic_videogame)
            model.delayItemList.add(item)
        }

        var item = model.DelayItemModel("电信服务器", "127.0.0.1", R.drawable.ic_server)
        model.delayItemList.add(item)
    }


}