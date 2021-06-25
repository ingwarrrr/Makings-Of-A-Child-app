package com.example.themakingsofachild.educator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import com.example.themakingsofachild.ChildListViewAdapter
import com.example.themakingsofachild.ChildProfileActivity
import com.example.themakingsofachild.Children
import com.example.themakingsofachild.R
import com.example.themakingsofachild.database.MyDBManager
import com.example.themakingsofachild.database.MyDBName
import com.example.themakingsofachild.director.BabyResultsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_educator.*
import kotlinx.android.synthetic.main.activity_inclination_test.textView2

class ChooseChildActivity : AppCompatActivity() {

    private var inclinationPosition: Int = 0
    private var groupPosition: Int = 0
    private var expextedInclination: String = ""
    private var groupOfChild: String = ""
    private var inclinationDefaultString: String = "Пока нет данных"
    private var childList = mutableListOf<Children>()
    private var listWihoutInclination = false
    private var role: String = ""

    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_child)

        role = intent.getStringExtra("role").toString()
        inclinationPosition = intent.getIntExtra("inclinationPosition", 0)
        groupPosition = intent.getIntExtra("groupPosition", 0)
        expextedInclination = intent.getStringExtra("inclination").toString()
        groupOfChild = intent.getStringExtra("groupOfChild").toString()
        listWihoutInclination = intent.getBooleanExtra("listWihoutInclination", false)

        when (listWihoutInclination) {
            true -> {
                textView.text = groupOfChild
            }
            false -> {
                textView.text = groupOfChild
                textView2.text = expextedInclination
            }
        }

        val listView = findViewById<ListView>(R.id.children)

        val assetsDB = MyDBManager(this)
        assetsDB.openDatabase()
        val children = assetsDB.readDataBase(MyDBName.TABLE_NAME[groupPosition])
        val childGender = assetsDB.readDataBaseForGender(MyDBName.TABLE_NAME[groupPosition])

        val prodAdapter =
            ChildListViewAdapter(
                this,
                R.layout.row_for_inclination_res,
                childList
            )

        val ref = FirebaseDatabase.getInstance().getReference("/child/Group$groupPosition/Inclination$inclinationPosition")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                index = 0
                p0.children.forEach {
                    val someChild = it.getValue(Children::class.java)
                    if (someChild != null && someChild.name == children[index]) {
                        inclinationDefaultString = someChild.inclination
                        if (listWihoutInclination) inclinationDefaultString = ""
                        childList.add(Children(someChild.name, inclinationDefaultString, someChild.child_gender))
                        Log.d("My_childList", someChild.name + " " + someChild.inclination)
                        index++
                    }
                    else if (someChild != null && someChild.name != children[index]) {
                        while (someChild.name != children[index]) {
                            childList.add(Children(children[index], "Пока нет данных", childGender[index]))
                            index++
                        }
                        inclinationDefaultString = someChild.inclination
                        if (listWihoutInclination) inclinationDefaultString = ""
                        childList.add(Children(someChild.name, inclinationDefaultString, someChild.child_gender))
                        index++
                    }
                }
                while (index != children.size) {
                    childList.add(Children(children[index], "Пока нет данных", childGender[index]))
                    index++
                }
                prodAdapter.notifyDataSetChanged();
            }

            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException();
            }
        })

        listView.adapter = prodAdapter
        assetsDB.closeDataBase()

        listView.setOnItemClickListener { parent, view, position, id ->
            assetsDB.closeDataBase()
            if (listWihoutInclination) {
                val intent = Intent(this, ChildProfileActivity::class.java)
                intent.putExtra("nameOfAChild", children[position]);
                intent.putExtra("childPosition", position);
                intent.putExtra("childGender", childGender[position]);
                intent.putExtra("groupOfChild", groupOfChild)
                intent.putExtra("groupPosition", groupPosition)
                intent.putExtra("listWihoutInclination", listWihoutInclination)
                intent.putExtra("inclinationPosition", inclinationPosition)
                intent.putExtra("inclination", expextedInclination)
                intent.putExtra("role", role)
                startActivity(intent)
            }
            else if (role == "director") {
                val intent = Intent(this, BabyResultsActivity::class.java)
                intent.putExtra("nameOfAChild", children[position]);
                intent.putExtra("childPosition", position);
                intent.putExtra("childGender", childGender[position]);
                intent.putExtra("groupOfChild", groupOfChild)
                intent.putExtra("role", role)
                intent.putExtra("groupPosition", groupPosition)
                intent.putExtra("inclinationPosition", inclinationPosition)
                intent.putExtra("inclination", expextedInclination)
                startActivity(intent)
            }
            else if (role == "educator") {
                val intent = Intent(this, InclinationTestActivity::class.java)
                intent.putExtra("nameOfAChild", children[position]);
                intent.putExtra("childPosition", position);
                intent.putExtra("childGender", childGender[position]);
                intent.putExtra("groupOfChild", groupOfChild)
                intent.putExtra("role", role)
                intent.putExtra("groupPosition", groupPosition)
                intent.putExtra("inclinationPosition", inclinationPosition)
                intent.putExtra("inclination", expextedInclination)
                startActivity(intent)
            }
        }
    }

    fun onClick(view: View) {
        val intent = Intent(this, MainProjectrActivity::class.java)
        intent.putExtra("inclinationOfChild", expextedInclination)
        intent.putExtra("inclinationPosition", inclinationPosition)
        intent.putExtra("groupOfChild", groupOfChild)
        intent.putExtra("groupPosition", groupPosition)
        intent.putExtra("listWihoutInclination", listWihoutInclination)
        intent.putExtra("role", role)
        startActivity(intent)
    }
}