package com.kdh.eatwd.presenter.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import com.kdh.eatwd.presenter.util.UiUtils

class CustomCircleProgressBar : View {
    // 생성자
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val paint = Paint()
    private val oval = RectF()
    private val displayMetrics = DisplayMetrics()
    private val density = resources.displayMetrics.density
    private var densityDpi: Int = 0
    private var pollCount: Int = 0;
    private val radius = 230
    private val align = 80
    private var centerX = 0f
    private var centerY = 0f
    private val textView: TextView = TextView(context)
//    private var textView = ""

    private val displayHalf by lazy {
        (UiUtils.dpToPx(context, densityDpi) / 2).toFloat()
    }

    private val displayTop by lazy {
        (UiUtils.dpToPx(context, densityDpi) * 0.10).toFloat()
    }

    private val displayLeft by lazy {
        (UiUtils.dpToPx(context, densityDpi) * 0.15).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        densityDpi = resources.displayMetrics.densityDpi
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 25f
            Paint.ANTI_ALIAS_FLAG
        }
        centerX = width * 0.5.toFloat()
        centerY = height * 0.5.toFloat()
        oval.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        canvas?.drawArc(oval, 0f, 360f, false, paint);
        paint.color = Color.RED
        canvas?.drawArc(oval, -90f, pollCount.toFloat(), false, paint);

//        canvas?.drawText(textView.text,0,textView.text.length,centerX- align,centerY,paint)
    }

    fun setPollCount(count: Int) {
        this.pollCount = count
        invalidate()

    }

//    fun setPollutionText(text : String){
//        paint.reset()
//        paint.color =Color.BLACK
//        paint.textSize = UiUtils.dpToPx(context,15).toFloat()
//        textView.text = text
//        invalidate()
//    }

}