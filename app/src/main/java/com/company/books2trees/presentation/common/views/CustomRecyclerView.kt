package com.company.books2trees.presentation.common.views

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CustomGridLayoutManager(val context: Context, _spanCount: Int) :
    GridLayoutManager(context, _spanCount) {

    /* Called when searching for a focusable view in the
     given direction has failed for the current
     content of the RecyclerView.*/
    override fun onFocusSearchFailed(
        focused: View,
        focusDirection: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): View? {
        return try {
            val fromPos = getPosition(focused)
            val nextPos = getNextViewPos(fromPos, focusDirection)
            findViewByPosition(nextPos)
        } catch (e: Exception) {
            null
        }
    }

    /* This method gives a LayoutManager an opportunity to
    intercept the initial focus search before the default
    behavior of FocusFinder is used.*/
    override fun onInterceptFocusSearch(focused: View, direction: Int): View? {
        return try {
            val fromPos = getPosition(focused)
            val nextPos = getNextViewPos(fromPos, direction)
            findViewByPosition(nextPos)
        } catch (e: Exception) {
            null
        }
    }


    private fun getNextViewPos(fromPos: Int, direction: Int): Int {
        val offset = calcOffsetToNextView(direction)

        if (hitBorder(fromPos, offset)) {
            return fromPos
        }

        return fromPos + offset
    }

    private fun calcOffsetToNextView(direction: Int): Int {
        val spanCount = this.spanCount
        val orientation = this.orientation

        // fixes arabic by inverting left and right layout focus
        val correctDirection = if (this.isLayoutRTL) {
            when (direction) {
                View.FOCUS_RIGHT -> View.FOCUS_LEFT
                View.FOCUS_LEFT -> View.FOCUS_RIGHT
                else -> direction
            }
        } else direction

        if (orientation == VERTICAL) {
            when (correctDirection) {
                View.FOCUS_DOWN -> {
                    return spanCount
                }

                View.FOCUS_UP -> {
                    return -spanCount
                }

                View.FOCUS_RIGHT -> {
                    return 1
                }

                View.FOCUS_LEFT -> {
                    return -1
                }
            }
        } else if (orientation == HORIZONTAL) {
            when (correctDirection) {
                View.FOCUS_DOWN -> {
                    return 1
                }

                View.FOCUS_UP -> {
                    return -1
                }

                View.FOCUS_RIGHT -> {
                    return spanCount
                }

                View.FOCUS_LEFT -> {
                    return -spanCount
                }
            }
        }
        return 0
    }

    private fun hitBorder(from: Int, offset: Int): Boolean {
        val spanCount = spanCount

        return if (abs(offset) == 1) {
            val spanIndex = from % spanCount
            val newSpanIndex = spanIndex + offset
            newSpanIndex < 0 || newSpanIndex >= spanCount
        } else {
            val newPos = from + offset
            newPos in spanCount..-1
        }
    }
}

class AutoFitRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    RecyclerView(context, attrs) {

    private val manager = CustomGridLayoutManager(context, 2) // THIS CONTROLS SPANS

    private var columnWidth = -1

    var spanCount = 0
        set(value) {
            field = value
            if (value > 0) {
                manager.spanCount = value
            }
        }

    val itemWidth: Int
        get() = measuredWidth / manager.spanCount

    init {
        if (attrs != null) {
            val attrsArray = intArrayOf(R.attr.columnWidth)
            val array = context.obtainStyledAttributes(attrs, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }

        layoutManager = manager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (columnWidth > 0) {
            val spanCount = 1.coerceAtLeast(measuredWidth / columnWidth)
            manager.spanCount = spanCount
        }
    }
}