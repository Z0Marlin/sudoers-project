package org.tensorflow.lite.examples.posenet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main_ar.*

class MainActivityAR : AppCompatActivity() {

    lateinit var fragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ar)
        val toolbar = findViewById (R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        fragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

    }

}