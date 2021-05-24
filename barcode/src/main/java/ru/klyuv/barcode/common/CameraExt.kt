package ru.klyuv.barcode.common

import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.media.Image
import androidx.annotation.ColorInt
import androidx.camera.core.AspectRatio
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val RATIO_4_3_VALUE = 4.0 / 3.0
private const val RATIO_16_9_VALUE = 16.0 / 9.0

/**
 *  [androidx.camera.core.impl.ImageAnalysisConfig] требует значение
 *  [androidx.camera.core.AspectRatio]. Сейчас он имеет значения 4:3 & 16:9.
 *
 *  Определение наиболее подходящего соотношения для размеров, указанных в @params,
 *  путем сравнения абсолютного соотношения предварительно просмотра с одинм из предоставленных
 *  значений
 *
 *  @param width - preview width
 *  @param height - preview height
 *  @return подходящее соотношение сторон
 */
fun aspectRatio(width: Int, height: Int): Int {
    val previewRatio = max(width, height).toDouble() / min(width, height)
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9
}

private val CHANNEL_RANGE = 0 until (1 shl 18)

fun Image.convertYuv420888ImageToBitmap(): Bitmap {
    require(this.format == ImageFormat.YUV_420_888) {
        "Unsupported image format $(image.format)"
    }

    val planes = this.planes

    // Из-за переменного шага невозможно узнать необходимые размеры YUV
    val yuvBytes = planes.map { plane ->
        val buffer = plane.buffer
        val yuvBytes = ByteArray(buffer.capacity())
        buffer[yuvBytes]
        buffer.rewind()
        yuvBytes
    }

    val yRowStride = planes[0].rowStride
    val uvRowStride = planes[1].rowStride
    val uvPixelStride = planes[1].pixelStride
    val width = this.width
    val height = this.height
    @ColorInt val argb8888 = IntArray(width * height)
    var i = 0
    for (y in 0 until height) {
        val pY = yRowStride * y
        val uvRowStart = uvRowStride * (y shr 1)
        for (x in 0 until width) {
            val uvOffset = (x shr 1) * uvPixelStride
            argb8888[i++] =
                yuvToRgb(
                    yuvBytes[0][pY + x].toIntUnsigned(),
                    yuvBytes[1][uvRowStart + uvOffset].toIntUnsigned(),
                    yuvBytes[2][uvRowStart + uvOffset].toIntUnsigned()
                )
        }
    }
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(argb8888, 0, width, 0, 0, width, height)
    return bitmap
}

fun rotateAndCrop(
    bitmap: Bitmap,
    imageRotationDegrees: Int,
    cropRect: Rect
): Bitmap {
    val matrix = Matrix()
    matrix.preRotate(imageRotationDegrees.toFloat())
    return Bitmap.createBitmap(
        bitmap,
        cropRect.left,
        cropRect.top,
        cropRect.width(),
        cropRect.height(),
        matrix,
        true
    )
}

@ColorInt
private fun yuvToRgb(nY: Int, nU: Int, nV: Int): Int {
    var nY = nY
    var nU = nU
    var nV = nV
    nY -= 16
    nU -= 128
    nV -= 128
    nY = nY.coerceAtLeast(0)

    // Это эквивалент значения с плавающей запятой.
    // Выполняем преобразование в целое число, потому-что некоторые Андроид девайсы
    // не имеют аппаратного обеспечения значений с плавающей запятой
    // nR = (int)(1.164 * nY + 2.018 * nU);
    // nG = (int)(1.164 * nY - 0.813 * nV - 0.391 * nU);
    // nB = (int)(1.164 * nY + 1.596 * nV);
    var nR = 1192 * nY + 1634 * nV
    var nG = 1192 * nY - 833 * nV - 400 * nU
    var nB = 1192 * nY + 2066 * nU

    // Фисируем значения перед нормализацией до 8 бит
    nR = nR.coerceIn(CHANNEL_RANGE) shr 10 and 0xff
    nG = nG.coerceIn(CHANNEL_RANGE) shr 10 and 0xff
    nB = nB.coerceIn(CHANNEL_RANGE) shr 10 and 0xff
    return -0x1000000 or (nR shl 16) or (nG shl 8) or nB
}

private fun Byte.toIntUnsigned(): Int {
    return toInt() and 0xFF
}