package com.kdh.eatwd.presenter.ui.custom

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.kdh.eatwd.databinding.DialogCaptureScreenBinding
import java.io.File

class CustomCaptureDialog(
    context: Context,
    private val captureImage: String,
    private val callBack : (String) -> Unit
) : Dialog(context) {


    private lateinit var binding: DialogCaptureScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCaptureScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        onClickEvent()

    }

    private fun initView() {
        with(binding) {
            //setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Glide.with(context).load(captureImage).into(ivShareScreen)
        }
    }

    private fun onClickEvent(){
        with(binding){
            btnShareScreen.setOnClickListener {
                callBack(captureImage)
            }
        }
    }

}