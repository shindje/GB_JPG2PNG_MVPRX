package com.example.gb_jpg2png_mvprx.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface MainView: MvpView {
    fun init()
    fun setJpgName(name: String?)
    fun setPngName(name: String?)
    fun clearJpgName()
    fun clearPngName()
    fun showChooseBtn()
    fun hideChooseBtn()
    fun showCancelBtn()
    fun hideCancelBtn()
    @OneExecution
    fun chooseJPG()
    @OneExecution
    fun showSuccessResult()
    @OneExecution
    fun showFailureResult()
    @OneExecution
    fun showCancelResult()
}