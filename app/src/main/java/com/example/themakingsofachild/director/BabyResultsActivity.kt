package com.example.themakingsofachild.director

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.themakingsofachild.Children
import com.example.themakingsofachild.R
import com.example.themakingsofachild.database.MyDBManager
import com.example.themakingsofachild.database.MyDBName
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_inclination_test.*

class BabyResultsActivity  : AppCompatActivity() {
    private val assetsDB = MyDBManager(this)
    private var sizeInclination: Int? = null
    var arrayResultInclination: IntArray? = null
    private var inclinationPosition: Int? = null
    private var nameOfChild: String = ""
    private var expextedInclination: String = ""
    private var childPosition: Int? = null
    private var groupPosition: Int? = null
    private var role: String = ""
    private var listWihoutInclination = false
    var incResult: List<Int> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_results)

        val imageView : ImageView = findViewById(R.id.iconIv)
        role = intent.getStringExtra("role").toString()
        listWihoutInclination = intent.getBooleanExtra("listWihoutInclination", false)
        inclinationPosition = intent.getIntExtra("inclinationPosition", 0)
        nameOfChild = intent.getStringExtra("nameOfAChild").toString()
        expextedInclination = intent.getStringExtra("inclination").toString()
        childPosition = intent.getIntExtra("childPosition", 0)
        groupPosition = intent.getIntExtra("groupPosition", 0)
        textView.text = nameOfChild
        textView2.text = expextedInclination

        assetsDB.openDatabase()
        val testInclinations = assetsDB.readDataBaseInc(MyDBName.TABLE_INC_NAME[inclinationPosition!!])
        sizeInclination = testInclinations.size
        arrayResultInclination = IntArray(sizeInclination!!)

        val childGender = assetsDB.readDataBaseForGender(MyDBName.TABLE_NAME[groupPosition!!])
        when(childGender[childPosition!!]){
            "male" -> imageView.setImageResource(R.drawable.boy)
            "female" -> imageView.setImageResource(R.drawable.girl)
        }

        val listView: ListView = findViewById(R.id.testInclinations)
        val prodAdapter = ArrayAdapter<String>(this, R.layout.my_simple_list_item_cheked, testInclinations)
        listView.adapter = prodAdapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE;

        val ref = FirebaseDatabase.getInstance().getReference("/child/Group$groupPosition/Inclination$inclinationPosition")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val someChild = it.getValue(Children::class.java)
                    if (someChild != null && someChild.name == nameOfChild) {
                        incResult = someChild.inclination.split(",").map { it.toInt() }
                        arrayResultInclination = incResult.toIntArray()
                        for (i in incResult.indices) {
                            if (incResult[i] > 0) listView.setItemChecked(i, true)
                        }
                        when(someChild.child_gender){
                            "male" -> imageView.setImageResource(R.drawable.boy)
                            "female" -> imageView.setImageResource(R.drawable.girl)
                        }
                        Log.d("My_childList", someChild.name + someChild.inclination)
                    }
                }
                prodAdapter.notifyDataSetChanged();
            }

            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException();
            }
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            val v = view as CheckedTextView

            if (v.isChecked)
                listView.setItemChecked(position, false)
            else
                listView.setItemChecked(position, true)
        }
    }

    fun onClick(view: View) {
        assetsDB.closeDataBase()
        finish()
    }
}