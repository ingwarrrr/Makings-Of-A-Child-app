package com.example.themakingsofachild

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.themakingsofachild.director.BabyResultsActivity
import com.example.themakingsofachild.educator.ChooseChildActivity
import com.example.themakingsofachild.educator.InclinationTestActivity
import com.example.themakingsofachild.educator.MainProjectrActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_inclination_test.*

class ChildProfileActivity : AppCompatActivity() {
    private var nameOfChild: String = ""
    private var groupOfChild: String = ""
    private var childGender: String = ""
    private var uselessInclinationPosition: Int = 0
    private var role: String = ""
    private var inclination: String = ""
    private var childPosition: Int = 0
    private var groupPosition: Int = 0
    private var listWihoutInclination = false
    private var inclinationList = mutableListOf<Inclinations>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_profile)

        nameOfChild = intent.getStringExtra("nameOfAChild").toString()
        inclination = intent.getStringExtra("inclination").toString()
        childGender = intent.getStringExtra("childGender").toString()
        groupOfChild = intent.getStringExtra("groupOfChild").toString()
        role = intent.getStringExtra("role").toString()
        uselessInclinationPosition = intent.getIntExtra("inclinationPosition", 0)
        listWihoutInclination = intent.getBooleanExtra("listWihoutInclination", false)
        childPosition = intent.getIntExtra("childPosition", 0)
        groupPosition = intent.getIntExtra("groupPosition", 0)
        textView.text = nameOfChild
        textView2.text = groupOfChild
        val typesOfInclinations = resources.getStringArray(R.array.types_of_inclinations)
        val imageView : ImageView = findViewById(R.id.iconIv)

        val listView: ListView = findViewById(R.id.testInclinations)
        val prodAdapter =
            ChildProhileAdapter(
                this,
                R.layout.row_for_child_profile,
                inclinationList
            )
        listView.adapter = prodAdapter

        for (inc in typesOfInclinations.indices) {
            val ref = FirebaseDatabase.getInstance().getReference("/child/Group$groupPosition/Inclination$inc")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        val incRes = it.getValue(Children::class.java)
                        if (incRes != null && incRes.name == nameOfChild) {
                            inclinationList.add(Inclinations(typesOfInclinations[inc], incRes.inclination))
                            Log.d("My_childList", incRes.name + " " + incRes.inclination)
                        }
                        else if (incRes == null) {
                            inclinationList.add(Inclinations(typesOfInclinations[inc], "Пока данных нет"))
                        }
                    }
                    when(childGender){
                        "male" -> imageView.setImageResource(R.drawable.boy)
                        "female" -> imageView.setImageResource(R.drawable.girl)
                    }
                    prodAdapter.notifyDataSetChanged();
                }

                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException();
                }
            })
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            when(role){
                "director" -> {
                    val intent = Intent(this, BabyResultsActivity::class.java)
                    intent.putExtra("nameOfAChild", nameOfChild);
                    intent.putExtra("childPosition", childPosition);
                    intent.putExtra("childGender", childGender);
                    intent.putExtra("groupOfChild", groupOfChild)
                    intent.putExtra("role", role)
                    intent.putExtra("groupPosition", groupPosition)
                    intent.putExtra("inclinationPosition", position)
                    intent.putExtra("inclination", typesOfInclinations[position])
                    intent.putExtra("listWihoutInclination", listWihoutInclination)
                    startActivity(intent)
                }
                "educator" -> {
                    val intent = Intent(this, InclinationTestActivity::class.java)
                    intent.putExtra("nameOfAChild", nameOfChild);
                    intent.putExtra("childPosition", childPosition);
                    intent.putExtra("childGender", childGender);
                    intent.putExtra("groupOfChild", groupOfChild)
                    intent.putExtra("role", role)
                    intent.putExtra("groupPosition", groupPosition)
                    intent.putExtra("inclinationPosition", position)
                    intent.putExtra("inclination", typesOfInclinations[position])
                    intent.putExtra("listWihoutInclination", listWihoutInclination)
                    startActivity(intent)
                }
            }
        }
    }

    fun onClick(view: View) {
        val intent = Intent(this, ChooseChildActivity::class.java)
        intent.putExtra("inclination", inclination)
        intent.putExtra("inclinationPosition", uselessInclinationPosition)
        intent.putExtra("groupOfChild", groupOfChild)
        intent.putExtra("groupPosition", groupPosition)
        intent.putExtra("listWihoutInclination", listWihoutInclination)
        intent.putExtra("role", role)
        startActivity(intent)
    }

    class Inclinations(var inclinationName: String, val inclinationResults: String){
        constructor() : this("","")
    }
}
