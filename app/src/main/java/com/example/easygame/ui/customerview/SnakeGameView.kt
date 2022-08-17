package com.example.easygame.ui.customerview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.example.easygame.model.Direction
import com.example.easygame.model.SnakeItem
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun Context.lifecycleOwner(): LifecycleOwner? {
    var context: Context? = this

    while (context != null || context !is LifecycleOwner) {
        val baseContext = (context as? ContextWrapper)?.baseContext
        context = if (baseContext == context) null else baseContext
    }
    return if (context is LifecycleOwner) context else null
}

class SnakeGameView(context: Context, attr: AttributeSet) : View(context, attr) {
    private val boxNum: Int = 20
    private val gap: Float = 5f
    private val margin: Float = 20f

    //var score: MutableLiveData<Int> = MutableLiveData(0)
    private var score = 0

    private val boxPaint = Paint().apply {
        color = Color.GRAY
    }
    private val snakePaint = Paint().apply {
        color = Color.BLUE
    }
    private val bonusPaint = Paint().apply {
        color = Color.RED
    }

    @SuppressLint("NewApi")
    private val scorePaint = Paint().apply {
        color = Color.valueOf(155f / 255f, 155f / 255f, 155f / 255f)
            .toArgb()
        textSize = 50f
        style = Paint.Style.FILL
    }

    private var borderClose = false
    private var snake = arrayListOf<SnakeItem>()
    private var direction = Direction.RIGHT
    private var playing = true

    private lateinit var bonus: SnakeItem

    init {
        snake.add(SnakeItem(10, 10))
        snake.add(SnakeItem(9, 10))
        snake.add(SnakeItem(8, 10))
        snake.add(SnakeItem(7, 10))
        randomBonus()
    }

    private fun randomBonus() {
        val x = (1..boxNum).random()
        val y = (1..boxNum).random()

        if (snake.any { item -> item.x == x && item.y == y }) {
            randomBonus()
        } else {
            bonus = SnakeItem(x, y)
        }
    }

    private fun restart() {
        snake.clear()

        snake.add(SnakeItem(10, 10))
        snake.add(SnakeItem(9, 10))
        snake.add(SnakeItem(8, 10))
        snake.add(SnakeItem(7, 10))

        score = 0
        playing = true

        direction = Direction.RIGHT
        randomBonus()
        invalidate()
    }

    private fun gameOver() {
        playing = false
        AlertDialog.Builder(context)
            .setTitle("GAME OVER")
            .setCancelable(false)
            .setPositiveButton("RESTART") { _, _ ->
                restart()
            }
            .show()
    }

    fun changeDirection(direction: Direction) {
        if (direction == Direction.RIGHT && this.direction == Direction.LEFT) {
            return
        }
        if (direction == Direction.LEFT && this.direction == Direction.RIGHT) {
            return
        }
        if (direction == Direction.UP && this.direction == Direction.DOWN) {
            return
        }
        if (direction == Direction.DOWN && this.direction == Direction.UP) {
            return
        }
        this.direction = direction
    }

    private fun getSpeed(): Long {
        return 100
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {

            val centerX = (this.width / 2).toFloat()
            val centerY = (this.height / 2).toFloat()

            val gameWidth = if (this.width > this.height) this.height - (margin * 2) else this.width - (margin * 2)
            val boxWidth = (gameWidth - (gap * 21)) / boxNum

            if (this.height > this.width) {
                canvas.drawText("得分: $score", margin, centerY - ((boxNum / 2) * (boxWidth + gap)) - margin, scorePaint)
            } else {

            }

            canvas.drawRect((centerX - ((boxNum / 2) * boxWidth) - (((boxNum + 1).toFloat() / 2) * gap)), (centerY - ((boxNum / 2) * boxWidth) - (((boxNum + 1).toFloat() / 2) * gap)), (centerX + ((boxNum / 2) * boxWidth) + (((boxNum + 1).toFloat() / 2) * gap)), (centerY - ((boxNum / 2) * boxWidth) - ((((boxNum + 1).toFloat() / 2) - 1) * gap)), boxPaint)
            canvas.drawRect((centerX - ((boxNum / 2) * boxWidth) - (((boxNum + 1).toFloat() / 2) * gap)), (centerY + ((boxNum / 2) * boxWidth) + ((((boxNum + 1).toFloat() / 2) - 1) * gap)), (centerX + ((boxNum / 2) * boxWidth) + (((boxNum + 1).toFloat() / 2) * gap)), (centerY + ((boxNum / 2) * boxWidth) + (((boxNum + 1).toFloat() / 2) * gap)), boxPaint)
            canvas.drawRect((centerX - ((boxNum / 2) * boxWidth) - (((boxNum + 1).toFloat() / 2) * gap)), (centerY + ((boxNum / 2) * boxWidth) + (((boxNum + 1).toFloat() / 2) * gap)), (centerX - ((boxNum / 2) * boxWidth) - ((((boxNum + 1).toFloat() / 2) - 1) * gap)), (centerY - ((boxNum / 2) * boxWidth) - (((boxNum + 1).toFloat() / 2) * gap)), boxPaint)
            canvas.drawRect((centerX + ((boxNum / 2) * boxWidth) + ((((boxNum + 1).toFloat() / 2) - 1) * gap)), (centerY + ((boxNum / 2) * boxWidth) + (((boxNum + 1).toFloat() / 2) * gap)), (centerX + ((boxNum / 2) * boxWidth) + (((boxNum + 1).toFloat() / 2) * gap)), (centerY - ((boxNum / 2) * boxWidth) - (((boxNum + 1).toFloat() / 2) * gap)), boxPaint)

            for (pos in snake) {
                canvas.drawRect((centerX - (((boxNum / 2 + 1).toFloat() - pos.x) * boxWidth) - ((((boxNum + 1).toFloat() / 2) - pos.x) * gap)), (centerY + (((boxNum / 2 + 1).toFloat() - pos.y) * boxWidth) + ((((boxNum + 1).toFloat() / 2) - pos.y) * gap)), (centerX - (((boxNum / 2).toFloat() - pos.x) * boxWidth) - ((((boxNum + 1).toFloat() / 2) - pos.x) * gap)), (centerY + (((boxNum / 2).toFloat() - pos.y) * boxWidth) + ((((boxNum + 1).toFloat() / 2) - pos.y) * gap)), snakePaint)
            }


            canvas.drawRect((centerX - (((boxNum / 2 + 1).toFloat() - bonus.x) * boxWidth) - ((((boxNum + 1).toFloat() / 2) - bonus.x) * gap)), (centerY + (((boxNum / 2 + 1).toFloat() - bonus.y) * boxWidth) + ((((boxNum + 1).toFloat() / 2) - bonus.y) * gap)), (centerX - (((boxNum / 2).toFloat() - bonus.x) * boxWidth) - ((((boxNum + 1).toFloat() / 2) - bonus.x) * gap)), (centerY + (((boxNum / 2).toFloat() - bonus.y) * boxWidth) + ((((boxNum + 1).toFloat() / 2) - bonus.y) * gap)), bonusPaint)

            if (playing) {
                postDelayed({
                    val scorePos = snake[snake.size - 1].copy()
                    for (index in snake.size - 1 downTo 0) {
                        if (index == 0) {
                            when (direction) {
                                Direction.RIGHT -> {
                                    snake[index].x++
                                }
                                Direction.LEFT -> {
                                    snake[index].x--
                                }
                                Direction.UP -> {
                                    snake[index].y++
                                }
                                Direction.DOWN -> {
                                    snake[index].y--
                                }
                            }

                            if (snake[index].x == bonus.x && snake[index].y == bonus.y) {
                                snake.add(scorePos)
                                score += 1
                                randomBonus()
                            }
                        } else {
                            snake[index].x = snake[index - 1].x
                            snake[index].y = snake[index - 1].y
                        }
                    }
                    if (snake.filterIndexed { index, item -> index != 0 && item.x == snake[0].x && item.y == snake[0].y }
                            .isNotEmpty()) {
                        gameOver()
                    }

//                        if (snake.slice(1 until snake.size)
//                                .contains(snake[0])
//                        ) {
//                            gameOver()
//                        }
                    if (borderClose) {
                        if (snake[0].x <= 0 || snake[0].x > boxNum || snake[0].y <= 0 || snake[0].y > boxNum) {
                            gameOver()
                        } else {
                            invalidate()
                        }
                    } else {
                        if (snake[0].x <= 0) {
                            snake[0].x = boxNum
                        } else if (snake[0].x > boxNum) {
                            snake[0].x = 1
                        } else if (snake[0].y <= 0) {
                            snake[0].y = boxNum
                        } else if (snake[0].y > boxNum) {
                            snake[0].y = 1
                        } else {

                        }
                        invalidate()
                    }
                }, getSpeed())
            }
        }
    }

    private var downX = 0f
    private var downY = 0f
    private var upX = 0f
    private var upY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    upX = event.x
                    upY = event.y
                    val x = abs(upX - downX)
                    val y = abs(upY - downY)
                    if (x == 0f || y == 0f) return true

                    val z = sqrt(x.toDouble() * x + y * y)
                    val angle = (asin(y / z) / Math.PI * 180).roundToInt()

                    if (upY < downY && angle > 45) {
                        changeDirection(Direction.UP)
                    } else if (upY > downY && angle > 45) {
                        changeDirection(Direction.DOWN)
                    } else if (upX < downX && angle <= 45) {
                        changeDirection(Direction.LEFT)
                    } else if (upX > downX && angle <= 45) {
                        changeDirection(Direction.RIGHT)
                    }
                    return true
                }
                else -> {

                }
            }
        }

        return super.onTouchEvent(event)
    }
}