/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.posenet

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import org.tensorflow.lite.examples.posenet.lib.KeyPoint
import org.tensorflow.lite.examples.posenet.lib.Person
import org.tensorflow.lite.examples.posenet.lib.Posenet


class TestActivity : AppCompatActivity() {
  /** Returns a resized bitmap of the drawable image.    */
  private fun drawableToBitmap(drawable: Drawable): Bitmap {
    val bitmap = Bitmap.createBitmap(257, 353, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    drawable.setBounds(0, 0, canvas.width, canvas.height)

    drawable.draw(canvas)
    return bitmap
  }
    private fun cosineSimilarity(vecA: List<KeyPoint>, vecB: List<KeyPoint>): Double{
        var dotProduct = 0.0
        var normAx = 0.0
        var normAy = 0.0
        var normBx = 0.0
        var normBy = 0.0

        for (i in 0 until vecA.size) {
            dotProduct += (vecA[i].position.x * vecB[i].position.x) + (vecA[i].position.y * vecB[i].position.y)
            normAx += Math.pow(vecA[i].position.x.toDouble(), 2.0)
            normAy += Math.pow(vecA[i].position.y.toDouble(), 2.0)

            normBx += Math.pow(vecB[i].position.x.toDouble(), 2.0)
            normBy += Math.pow(vecB[i].position.y.toDouble(), 2.0)
        }
        return dotProduct / (Math.sqrt(normAx + normAy) * Math.sqrt(normBx + normBy))
    }

    private fun matchPosture(actualPose: Person, userPose: Person): Boolean {
        Log.d("actualPoseVector: ", actualPose.keyPoints[0].position.x.toString())
        Log.d("userPoseVector", userPose.keyPoints[0].position.x.toString())

        var cosineScore = cosineSimilarity(actualPose.keyPoints, userPose.keyPoints)
        var euclidDist = Math.sqrt(2*(1 - cosineScore))

        return (euclidDist < .14)
    }

  /** Calls the Posenet library functions.    */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_test)

    val sampleImageView = findViewById<ImageView>(R.id.image)
    val drawedImage = ResourcesCompat.getDrawable(resources, R.drawable.image, null)
    val imageBitmap = drawableToBitmap(drawedImage!!)
    sampleImageView.setImageBitmap(imageBitmap)
    val posenet = Posenet(this.applicationContext)
    val person = posenet.estimateSinglePose(imageBitmap)

    // Draw the keypoints over the image.
    val paint = Paint()
    paint.color = Color.RED
    val size = 2.0f

    val mutableBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(mutableBitmap)
    for (keypoint in person.keyPoints) {
      canvas.drawCircle(
        keypoint.position.x.toFloat(),
        keypoint.position.y.toFloat(), size, paint
      )
    }
    sampleImageView.adjustViewBounds = true
    sampleImageView.setImageBitmap(mutableBitmap)



  }
}
