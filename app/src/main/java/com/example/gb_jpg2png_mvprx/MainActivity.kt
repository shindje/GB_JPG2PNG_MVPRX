package com.example.gb_jpg2png_mvprx

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import com.example.gb_jpg2png_mvprx.mvp.presenter.MainPresenter
import com.example.gb_jpg2png_mvprx.mvp.view.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    val presenter by moxyPresenter {
        MainPresenter(AndroidSchedulers.mainThread(), SDKConverter(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_choose_jpg.setOnClickListener { presenter.onChooseBtnClick() }
        btn_cancel.setOnClickListener { presenter.onCancel() }
    }

    override fun init() {
    }

    override fun clearJpgName() {
        tv_jpg.setText(getString(R.string.choose_jpg))
    }

    override fun clearPngName() {
        tv_png.setText(getString(R.string.png_file))
    }

    override fun setJpgName(name: String?) {
        tv_jpg.setText(name)
    }

    override fun setPngName(name: String?) {
        tv_png.setText(name)
    }

    override fun showSuccessResult() {
        Toast.makeText(this, "Converting successfully finished", Toast.LENGTH_SHORT).show()
        presenter.onFinish()
    }

    override fun showFailureResult() {
        Toast.makeText(this, "Converting finished with failure", Toast.LENGTH_SHORT).show()
        presenter.onFinish()
    }

    override fun showCancelResult() {
        Toast.makeText(this, "Converting cancelled", Toast.LENGTH_SHORT).show()
        presenter.onFinish()
    }

    override fun showChooseBtn() {
        btn_choose_jpg.visibility = View.VISIBLE
    }

    override fun hideChooseBtn() {
        btn_choose_jpg.visibility = View.INVISIBLE
    }

    override fun showCancelBtn() {
        btn_cancel.visibility = View.VISIBLE
    }

    override fun hideCancelBtn() {
        btn_cancel.visibility = View.INVISIBLE
    }

    override fun chooseJPG() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == OPEN_DOCUMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { documentUri ->
                contentResolver.takePersistableUriPermission(
                    documentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                val cursor: Cursor? = contentResolver.query(
                    documentUri, null, null, null, null, null)

                var name: String? = null
                cursor?.use {
                    if (it.moveToFirst()) {
                        name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }

                presenter.onChooseJPG(name, documentUri.toString())
            }
        }
    }
}

private const val OPEN_DOCUMENT_REQUEST_CODE = 0x33