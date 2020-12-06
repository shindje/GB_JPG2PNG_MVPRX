package com.example.gb_jpg2png_mvprx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

class SDKConverter(val context: Context): IConverter {
    override fun convert(jpgName: String?, jpg: String): Single<String?> =
        Single.create<String>() {
            var returnName: String? = null
            context.contentResolver.openFileDescriptor(Uri.parse(jpg), "r")?.apply {
                val image = BitmapFactory.decodeFileDescriptor(this.fileDescriptor)

                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {

                }

                if (!it.isDisposed()) {
                    val pngName = (jpgName?.let { jpgName.substring(0, jpgName.lastIndexOf('.')) }
                        ?: "newFile") + ".png"
                    val png = File(
                        context.getExternalFilesDir(DIRECTORY_PICTURES)
                            ?.getPath() + File.separator + pngName
                    )
                    png.createNewFile();
                    val out = FileOutputStream(png);
                    image.compress(Bitmap.CompressFormat.PNG, 100, out)
                    out.close()
                    returnName = pngName
                }

                this.close()
            }
            it.onSuccess(returnName)
        }.subscribeOn(Schedulers.io())
}