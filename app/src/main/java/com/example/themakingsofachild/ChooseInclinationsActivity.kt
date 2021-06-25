package com.example.themakingsofachild

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class ChooseInclinationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_inclinations)

        val listView = findViewById<ListView>(R.id.typesOfInclinations)
        val typesOfInclinations = resources.getStringArray(R.array.types_of_inclinations)
        val prodAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, typesOfInclinations)
        listView.adapter = prodAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as String
            val intent = Intent()
            intent.putExtra("myItem", item);
            intent.putExtra("myItemPosition", position);
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}