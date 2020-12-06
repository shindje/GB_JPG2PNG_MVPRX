package com.example.gb_jpg2png_mvprx

import io.reactivex.rxjava3.core.Single

interface IConverter {
    fun convert(jpgName: String?, jpg: String): Single<String?>
}