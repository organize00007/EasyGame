package com.example.easygame.ui.customerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.easygame.model.Direction
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("NewApi")
class TetrisGameView(context: Context, attr: AttributeSet) : View(context, attr) {
    private val TAG = "TetrisGameView"

    private lateinit var map: List<List<Int>>
    private val IPaint = Paint().apply {
        color = Color.valueOf(0f / 255f, 240f / 255f, 240f / 255f)
            .toArgb()
    }
    private val IShape = TetrisShape(IPaint, 1, 4, listOf(listOf(1), listOf(1), listOf(1), listOf(1)))
    private val JPaint = Paint().apply {
        color = Color.valueOf(0f / 255f, 0f / 255f, 120f / 255f)
            .toArgb()
    }
    private val JShape = TetrisShape(JPaint, 3, 2, listOf(listOf(1, 0, 0), listOf(1, 1, 1)))
    private val LPaint = Paint().apply {
        color = Color.valueOf(240f / 255f, 160f / 255f, 0f / 255f)
            .toArgb()
    }
    private val LShape = TetrisShape(LPaint, 3, 2, listOf(listOf(0, 0, 1), listOf(1, 1, 1)))
    private val OPaint = Paint().apply {
        color = Color.valueOf(240f / 255f, 240f / 255f, 0f / 255f)
            .toArgb()
    }
    private val OShape = TetrisShape(OPaint, 2, 2, listOf(listOf(1, 1), listOf(1, 1)))
    private val SPaint = Paint().apply {
        color = Color.valueOf(0f / 255f, 240f / 255f, 0f / 255f)
            .toArgb()
    }
    private val SShape = TetrisShape(SPaint, 3, 2, listOf(listOf(0, 1, 1), listOf(1, 1, 0)))
    private val TPaint = Paint().apply {
        color = Color.valueOf(160f / 255f, 22f / 255f, 89f / 255f)
            .toArgb()
    }
    private val TShape = TetrisShape(TPaint, 3, 2, listOf(listOf(0, 1, 0), listOf(1, 1, 1)))
    private val ZPaint = Paint().apply {
        color = Color.valueOf(240f / 255f, 0f / 255f, 0f / 255f)
            .toArgb()
    }
    private val ZShape = TetrisShape(ZPaint, 3, 2, listOf(listOf(1, 1, 0), listOf(0, 1, 1)))

    private val backgroundPaint = Paint().apply {
        color = Color.valueOf(155f / 255f, 155f / 255f, 155f / 255f)
            .toArgb()
    }
    private val shapeList = listOf(IShape, JShape, LShape, OShape, SShape, TShape, ZShape)

    private val gap = 10f
    private var boxWidth = 0f

    private var currentShapeLocation: TetrisShapeLocation? = null

    init {
        initMap()

        this.setOnClickListener {
            currentShapeLocation?.let {
                it.direction = when (it.direction) {
                    Direction.UP -> {
                        Direction.RIGHT
                    }
                    Direction.RIGHT -> {
                        Direction.DOWN
                    }
                    Direction.DOWN -> {
                        Direction.LEFT
                    }
                    Direction.LEFT -> {
                        Direction.UP
                    }
                }
                invalidate()
            }
        }
    }

    private fun initMap() {
        val tempMap = arrayListOf<List<Int>>()
        for (h in 0 until 20) {
            val tempW = arrayListOf<Int>()
            for (w in 0 until 10) {
                tempW.add(0)
            }
            tempMap.add(tempW)
        }
        map = tempMap
    }

    val timer = Timer().schedule(object : TimerTask() {
        override fun run() {
            currentShapeLocation?.let {
                it.y = it.y + 1
            }
            invalidate()
        }
    }, 0, 200)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        boxWidth = if ((width - (11 * gap)) / 14 > (height - (21 * gap)) / 20) (height - (21 * gap)) / 20 else (width - (11 * gap)) / 14

        canvas?.let {
            (0 until 10).forEach { x ->
                (0 until 20).forEach { y ->
                    canvas.drawRect((gap * (x + 1)) + (boxWidth * x), (gap * (y + 1)) + (boxWidth * y), (gap * (x + 1)) + (boxWidth * (x + 1)), (gap * (y + 1)) + (boxWidth * y) + boxWidth, backgroundPaint)
                }
            }

            if (currentShapeLocation == null) getRandomShape()
            else {
                currentShapeLocation?.let { currentShape ->

                    if (!currentShape.enableDrop()) {
//                        val tempTemplate = currentShape.getCurrentTemplate()
//                        Log.e(TAG, "${currentShape.x}, ${currentShape.y}")
                        currentShapeLocation = null
                    }
                }
            }
            Log.e(TAG, "${currentShapeLocation?.x}, ${currentShapeLocation?.y}")
            currentShapeLocation?.draw(canvas)
        }
    }

    private fun getRandomShape() {
        currentShapeLocation = TetrisShapeLocation(shapeList[0], 5, 0)

//        val tempShape = shapeList[(shapeList.indices).random()]
//        currentShapeLocation = TetrisShapeLocation(tempShape, 5, 0)
    }


    data class TetrisShape(val paint: Paint, val width: Int, val height: Int, val template: List<List<Int>>)
    data class TetrisShapeLocation(val tetrisShape: TetrisShape, var x: Int, var y: Int, var direction: Direction = Direction.UP) {
        init {
            x -= tetrisShape.width / 2
        }
    }

    private fun TetrisShapeLocation.getCurrentTemplate(): List<List<Int>> {
        val result = arrayListOf<ArrayList<Int>>()
        return when (this.direction) {
            Direction.UP -> {
                this.tetrisShape.template
            }
            Direction.DOWN -> {
                for (h in 0 until this.tetrisShape.template.size) {
                    val tempList = arrayListOf<Int>()
                    for (w in 0 until this.tetrisShape.template[h].size) {
                        tempList.add(this.tetrisShape.template[this.tetrisShape.height - 1 - h][this.tetrisShape.width - 1 - w])
                    }
                    result.add(tempList)
                }
                result.toList()
            }
            Direction.LEFT -> {
                for (h in 0 until this.tetrisShape.template.size) {
                    for (w in 0 until this.tetrisShape.template[h].size) {
                        val tempList = if (result.size < w + 1) {
                            val temp = arrayListOf<Int>()
                            result.add(temp)
                            temp
                        } else result[w]
                        tempList.add(this.tetrisShape.template[h][this.tetrisShape.width - 1 - w])
                    }
                }
                result.toList()
            }
            Direction.RIGHT -> {
                for (h in 0 until this.tetrisShape.template.size) {
                    for (w in 0 until this.tetrisShape.template[h].size) {
                        val tempList = if (result.size < w + 1) {
                            val temp = arrayListOf<Int>()
                            result.add(temp)
                            temp
                        } else result[w]
                        tempList.add(this.tetrisShape.template[this.tetrisShape.height - 1 - h][w])
                    }
                }
                result.toList()
            }
        }
    }

    private fun TetrisShapeLocation.enableDrop(): Boolean {


        return when (this.direction) {
            Direction.UP, Direction.DOWN -> {
                this.y + this.tetrisShape.height <= 20
            }
            else -> {
                this.y + this.tetrisShape.width <= 20
            }
        }
    }

    private fun TetrisShapeLocation.draw(canvas: Canvas) {
        val template = this.getCurrentTemplate()
        for (h in template.indices) {
            for (w in 0 until template[h].size) {
                if (template[h][w] == 1) {
                    drawShape(canvas, this.tetrisShape.paint, x + w, y + h)
                }
            }
        }
    }

    private fun TetrisShapeLocation.drawShape(canvas: Canvas, paint: Paint, x: Int, y: Int) {
        canvas.drawRect((gap * (x + 1)) + (boxWidth * (x)), (gap * (y + 1)) + (boxWidth * (y)), (gap * (x + 1)) + (boxWidth * (x + 1)), (gap * (y + 1)) + (boxWidth * (y)) + boxWidth, paint)
    }
}