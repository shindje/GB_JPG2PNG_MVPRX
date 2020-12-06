package com.example.gb_jpg2png_mvprx.mvp.presenter

import com.example.gb_jpg2png_mvprx.IConverter
import com.example.gb_jpg2png_mvprx.mvp.view.MainView
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter

class MainPresenter(val mainThread: Scheduler, val converter: IConverter): MvpPresenter<MainView>() {

    var disposable:Disposable? = null

    fun onChooseBtnClick() {
        viewState.clearJpgName()
        viewState.clearPngName()
        viewState.chooseJPG()
    }


    fun onChooseJPG(name: String?, jpg: String) {
        viewState.showCancelBtn()
        viewState.hideChooseBtn()

        name?.apply { viewState.setJpgName(this) }
        viewState.clearPngName()
        disposable = converter.convert(name, jpg)
            .observeOn(mainThread)
            .subscribe ({
                viewState.setPngName(it)
                viewState.showSuccessResult()
                viewState.showChooseBtn()
                viewState.hideCancelBtn()
            }, {
                it.printStackTrace()
                viewState.showFailureResult()
                viewState.showChooseBtn()
                viewState.hideCancelBtn()
            })
    }

    fun onCancel() {
        disposable?.dispose()
        viewState.clearJpgName()
        viewState.showCancelResult()
        viewState.showChooseBtn()
        viewState.hideCancelBtn()
    }

    fun onFinish() {
        disposable = null
    }
}