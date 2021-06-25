package com.example.themakingsofachild.educator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.themakingsofachild.ChildProfileActivity
import com.example.themakingsofachild.Children
import com.example.themakingsofachild.R
import com.example.themakingsofachild.database.MyDBManager
import com.example.themakingsofachild.database.MyDBName
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_educator.*
import kotlinx.android.synthetic.main.activity_inclination_test.*
import kotlinx.android.synthetic.main.activity_inclination_test.textView
import kotlinx.android.synthetic.main.activity_inclination_test.textView2


class InclinationTestActivity : AppCompatActivity() {
    private val assetsDB = MyDBManager(this)
    private var sizeInclination: Int? = null
    var arrayResultInclination: IntArray? = null
    private var inclinationPosition: Int? = null
    private var nameOfChild: String = ""
    private var expextedInclination: String = ""
    private var ResultInclination: String = ""
    private var childrenGender: String = ""
    private var childPosition: Int? = null
    private var groupPosition: Int = 0
    private var groupOfChild: String = ""
    var incResult: List<Int> = listOf()
    private var role: String = ""
    private var listWihoutInclination = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inclination_test)

        val imageView : ImageView = findViewById(R.id.iconIv)
        inclinationPosition = intent.getIntExtra("inclinationPosition", 0)
        role = intent.getStringExtra("role").toString()
        listWihoutInclination = intent.getBooleanExtra("listWihoutInclination", false)
        nameOfChild = intent.getStringExtra("nameOfAChild").toString()
        childrenGender = intent.getStringExtra("childGender").toString()
        expextedInclination = intent.getStringExtra("inclination").toString()
        childPosition = intent.getIntExtra("childPosition", 0)
        groupPosition = intent.getIntExtra("groupPosition", 0)
        groupOfChild = intent.getStringExtra("groupOfChild").toString()
        textView.text = nameOfChild
        textView2.text = expextedInclination

        assetsDB.openDatabase()
        val testInclinations = assetsDB.readDataBaseInc(MyDBName.TABLE_INC_NAME[inclinationPosition!!])
        sizeInclination = testInclinations.size
        arrayResultInclination = IntArray(sizeInclination!!)

        val childGender = assetsDB.readDataBaseForGender(MyDBName.TABLE_NAME[groupPosition])
        when(childGender[childPosition!!]){
            "male" -> imageView.setImageResource(R.drawable.boy)
            "female" -> imageView.setImageResource(R.drawable.girl)
        }

        val listView: ListView = findViewById(R.id.testInclinations)
        val prodAdapter = ArrayAdapter<String>(this, R.layout.my_simple_list_item_cheked, testInclinations)
        listView.adapter = prodAdapter

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
                        Log.d("My_childList", someChild.name + someChild.inclination)
                    }
                }
                prodAdapter.notifyDataSetChanged();
            }

            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException();
            }
        })

        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE;
        listView.setOnItemClickListener { parent, view, position, id ->
            val v = view as CheckedTextView
            val currentCheck = v.isChecked

            if (currentCheck)
                arrayResultInclination!![position] = 1
            else
                arrayResultInclination!![position] = 0
        }
    }

    fun onClick(view: View) {
        for (i in arrayResultInclination?.indices!!) {
            ResultInclination += arrayResultInclination!![i]
            if (i < arrayResultInclination!!.size - 1) ResultInclination += ","
        }
        assetsDB.closeDataBase()
        saveTestResultsToDatabase()
        when (listWihoutInclination) {
            false -> {
                val intent = Intent(this, ChooseChildActivity::class.java)
                intent.putExtra("inclinationPosition", inclinationPosition)
                intent.putExtra("groupPosition", groupPosition)
                intent.putExtra("inclination", expextedInclination)
                intent.putExtra("groupOfChild", groupOfChild)
                intent.putExtra("role", role)
                intent.putExtra("listWihoutInclination", listWihoutInclination)
                startActivity(intent)
            }
            true -> {
                val intent = Intent(this, ChildProfileActivity::class.java)
                intent.putExtra("nameOfAChild", nameOfChild);
                intent.putExtra("childPosition", childPosition);
                intent.putExtra("childGender", childrenGender);
                intent.putExtra("groupOfChild", groupOfChild)
                intent.putExtra("role", role)
                intent.putExtra("groupPosition", groupPosition)
                intent.putExtra("inclinationPosition", inclinationPosition)
                intent.putExtra("inclination", expextedInclination)
                intent.putExtra("listWihoutInclination", listWihoutInclination)
                startActivity(intent)
            }
        }
        finish()
    }

    private fun saveTestResultsToDatabase() {
        val children = Children(nameOfChild, ResultInclination, childrenGender)
        val childId = if (childPosition!! < 10) "00$childPosition" else "0$childPosition"
        val refPath = "child/Group$groupPosition/Inclination$inclinationPosition/Child$childId"

        val database = Firebase.database
        val myRef = database.getReference(refPath)

        myRef.setValue(children)
        Toast.makeText(this, "Данные сохранены!", Toast.LENGTH_SHORT).show()
    }

    fun onClickBack(view: View) {
        assetsDB.closeDataBase()
        when (listWihoutInclination) {
            false -> {
                val intent = Intent(this, ChooseChildActivity::class.java)
                intent.putExtra("role", role)
                intent.putExtra("inclinationPosition", inclinationPosition)
                intent.putExtra("groupPosition", groupPosition)
                intent.putExtra("inclination", expextedInclination)
                intent.putExtra("groupOfChild", groupOfChild)
                intent.putExtra("listWihoutInclination", listWihoutInclination)
                startActivity(intent)
            }
            true -> {
                val intent = Intent(this, ChildProfileActivity::class.java)
                intent.putExtra("nameOfAChild", nameOfChild);
                intent.putExtra("childPosition", childPosition);
                intent.putExtra("childGender", childrenGender);
                intent.putExtra("groupOfChild", groupOfChild)
                intent.putExtra("role", role)
                intent.putExtra("groupPosition", groupPosition)
                intent.putExtra("inclinationPosition", inclinationPosition)
                intent.putExtra("inclination", expextedInclination)
                intent.putExtra("listWihoutInclination", listWihoutInclination)
                startActivity(intent)
            }
        }
    }
}