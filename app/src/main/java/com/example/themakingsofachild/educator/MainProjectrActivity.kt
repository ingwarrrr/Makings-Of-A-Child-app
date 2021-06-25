package com.example.themakingsofachild.educator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.themakingsofachild.ChooseGroupActivity
import com.example.themakingsofachild.ChooseInclinationsActivity
import com.example.themakingsofachild.R
import kotlinx.android.synthetic.main.activity_educator.*

class MainProjectrActivity : AppCompatActivity() {

    private val ACTIVITY_REQUEST_CODE1 = 0
    private val ACTIVITY_REQUEST_CODE2 = 1
    private var returnGroupPosition: Int? = null
    private var returnInclinationPosition: Int? = null
    private var listWihoutInclination = false
    private var role: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educator)

        val textView = findViewById<TextView>(R.id.textView)
        val textView2 = findViewById<TextView>(R.id.textView2)
        role = intent.getStringExtra("role").toString()
        returnInclinationPosition = intent.getIntExtra("inclinationPosition", 0)
        returnGroupPosition = intent.getIntExtra("groupPosition", 0)
        listWihoutInclination = intent.getBooleanExtra("listWihoutInclination", false)
        val inclinationOfChild = intent.getStringExtra("inclinationOfChild")
        textView2.text = inclinationOfChild
        val groupOfChild = intent.getStringExtra("groupOfChild")
        textView.text = groupOfChild
        if (textView.text != "" && textView2.text != "") {
            textView.visibility = View.VISIBLE
            textView2.visibility = View.VISIBLE
            setImageToInclinationImageV()
        }
        if (listWihoutInclination) {
            val imageView : ImageView = findViewById(R.id.iconIv)
            imageView.visibility = View.GONE
            textView2.visibility = View.GONE
            textView2.text = ""
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.button -> {
                val intent = Intent(this, ChooseGroupActivity::class.java)
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE1)
            }
            R.id.button2 -> {
                val intent = Intent(this, ChooseInclinationsActivity::class.java)
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE2)
            }
            R.id.button3 -> {
                when ("") {
                    textView.text ->
                        Toast.makeText(this, "Выберите группу!", Toast.LENGTH_LONG).show()
                    else -> {
                        listWihoutInclination = true
                        val intent = Intent(this, ChooseChildActivity::class.java)
                        intent.putExtra("groupPosition", returnGroupPosition)
                        intent.putExtra("inclinationPosition", returnInclinationPosition)
                        intent.putExtra("groupOfChild", textView.text.toString())
                        intent.putExtra("listWihoutInclination", listWihoutInclination)
                        intent.putExtra("role", role)
                        startActivity(intent)
                    }
                }
            }
            R.id.button4 -> {
                when ("") {
                    textView.text ->
                        Toast.makeText(this, "Выберите группу!", Toast.LENGTH_LONG).show()
                    textView2.text ->
                        Toast.makeText(this, "Выберите наклонность!", Toast.LENGTH_LONG).show()
                    else -> {
                        listWihoutInclination = false
                        val intent = Intent(this, ChooseChildActivity::class.java)
                        intent.putExtra("inclinationPosition", returnInclinationPosition)
                        intent.putExtra("groupPosition", returnGroupPosition)
                        intent.putExtra("inclination", textView2.text.toString())
                        intent.putExtra("groupOfChild", textView.text.toString())
                        intent.putExtra("listWihoutInclination", listWihoutInclination)
                        intent.putExtra("role", role)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACTIVITY_REQUEST_CODE1) {
            if (resultCode == Activity.RESULT_OK) {
                val returnString = data!!.getStringExtra("myItem")
                returnGroupPosition = data.getIntExtra("myItemPosition", 0)

                val textView = findViewById<TextView>(R.id.textView)
                textView.text = returnString
                textView.visibility = View.VISIBLE
            }
        }
        if (requestCode == ACTIVITY_REQUEST_CODE2) {
            if (resultCode == Activity.RESULT_OK) {
                val returnString = data!!.getStringExtra("myItem")
                returnInclinationPosition = data.getIntExtra("myItemPosition", 0)

                val textView2 = findViewById<TextView>(R.id.textView2)
                textView2.text = returnString
                textView2.visibility = View.VISIBLE
                setImageToInclinationImageV()
            }
        }
    }

    private fun setImageToInclinationImageV() {
        val imageView : ImageView = findViewById(R.id.iconIv)
        when(returnInclinationPosition) {
            0 -> imageView.setImageResource(R.drawable.intelegent)
            1 -> imageView.setImageResource(R.drawable.logic)
            2 -> imageView.setImageResource(R.drawable.phisic)
            3 -> imageView.setImageResource(R.drawable.art_int)
            4 -> imageView.setImageResource(R.drawable.sport)
            5 -> imageView.setImageResource(R.drawable.music)
            6 -> imageView.setImageResource(R.drawable.art)
            7 -> imageView.setImageResource(R.drawable.leader)
            8 -> imageView.setImageResource(R.drawable.filolog)
        }
        imageView.visibility = View.VISIBLE
    }
}