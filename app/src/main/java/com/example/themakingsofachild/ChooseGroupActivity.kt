package com.example.themakingsofachild

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ChooseGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_group)

        val listView = findViewById<ListView>(R.id.typesOfGroupList)
        val typesOfGroups = resources.getStringArray(R.array.types_of_groups)
        val prodAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, typesOfGroups)
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