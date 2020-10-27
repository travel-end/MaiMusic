package wind.widget.edittext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import wind.widget.R


class SearchEditText : AppCompatEditText {
    private var mClearBitmap: Bitmap? = null
    private var mPaint: Paint? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mBitWidth: Int = 0
    private var mBitHeight: Int = 0
    private var showClose: Boolean = false// 是否显示清除icon
    private var hideClose: Boolean = false
    private var scale: Float = 0.8.toFloat()
    private var padding: Float = 0.toFloat()
    private var mDrawWidth: Float = 0.toFloat()
    private var onSearchEditTextListener:OnSearchEditTextListener?=null

    private var hasScale: Boolean = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        var clearIcon = 0
        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.SearchEditText)
            try {
                clearIcon = a.getResourceId(R.styleable.SearchEditText_clearIcon, 0)
                scale = a.getFloat(R.styleable.SearchEditText_scaleSize, 0f)
                mDrawWidth = a.getDimension(R.styleable.SearchEditText_drawableWidth, 0.0f)
            } finally {
                a.recycle()
            }
        }
        val bfoOptions = BitmapFactory.Options()
        bfoOptions.inScaled = false
        mClearBitmap = if (clearIcon != 0) {
            BitmapFactory.decodeResource(resources, clearIcon, bfoOptions)
        } else
            BitmapFactory.decodeResource(resources, R.drawable.cha, bfoOptions)

        if (scale == 0f) {
            scale = DEFAUT_SCALE
        }

        mPaint = Paint()

        addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                showClose = !TextUtils.isEmpty(s)
                invalidate()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                onSearchEditTextListener?.afterTextChange(s)
            }
        })

        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            showClose = if (hasFocus) {
                !TextUtils.isEmpty(text!!.toString())
            } else {
                false
            }
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (showClose && event.x > width - height + padding
                && event.x < width - padding
                && event.y > padding
                && event.y < height - padding
            ) {
                setText("")
                onSearchEditTextListener?.onClear()
            }
        }
        return super.onTouchEvent(event)
    }
    interface OnSearchEditTextListener {
        fun onClear()
        fun afterTextChange(s:Editable?)
    }
    fun setOnSearchEditTextListener(listener:OnSearchEditTextListener) {
        this.onSearchEditTextListener = listener
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (showClose && !hideClose) {
            val mSrcRect = Rect(0, 0, mBitWidth, mBitWidth)
            val mDestRect = RectF(
                mWidth.toFloat() - mBitWidth.toFloat() - padding,
                padding, mWidth - padding, mHeight - padding
            )
            canvas.drawBitmap(mClearBitmap!!, mSrcRect, mDestRect, mPaint)
        }
    }

    private fun deal() {
        if (!hasScale) {
            val width = mClearBitmap!!.width
            val height = mClearBitmap!!.height
            if (mDrawWidth == 0f) {
                padding = mHeight.toFloat() * (1 - scale) / 2
                mBitWidth = (mHeight - 2 * padding).toInt()
                mBitHeight = mBitWidth
            } else {
                if (mDrawWidth > mHeight) {
                    mDrawWidth = mHeight.toFloat()
                }
                padding = (mHeight - mDrawWidth) / 2
                mBitWidth = mDrawWidth.toInt()
                mBitHeight = mDrawWidth.toInt()
            }
            val scaleWidth = mBitWidth.toFloat() / width
            val scaleHeight = mBitHeight.toFloat() / height
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            mClearBitmap = Bitmap.createBitmap(mClearBitmap!!, 0, 0, width, height, matrix, true)
            hasScale = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        deal()

    }

    companion object {
        private val DEFAUT_SCALE = 1f
    }

    fun hideClose() {
        hideClose = true
        invalidate()
    }

    fun showClose() {
        hideClose = false
        invalidate()
    }
}
