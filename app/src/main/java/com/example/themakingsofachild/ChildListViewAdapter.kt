package com.example.themakingsofachild
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ChildListViewAdapter(var mCtx:Context, var resource:Int, var items:List<Children>)
    :ArrayAdapter<Children>( mCtx , resource , items ){

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var result: Double = -1.0
        val layoutInflater :LayoutInflater = LayoutInflater.from(mCtx)
        val childInclination: String

        val view : View = layoutInflater.inflate(resource , null )
        val imageView : ImageView = view.findViewById(R.id.iconIv)
        val textView : TextView = view.findViewById(R.id.titleTv)
        val textView1 : TextView = view.findViewById(R.id.descTv)

        val person : Children = items[position]

        if (person.inclination != "Пока нет данных" && person.inclination != "") {
            val incResult: List<Int> = person.inclination
                .split(",")
                .map { it.toInt() }
            result = incResult.sum().toDouble() / incResult.size
        }

        when {
            person.inclination == "" -> childInclination = person.inclination
            result > 0.7 -> childInclination = "Высокая наклонность"
            result > 0.3 -> childInclination = "Средняя наклонность"
            result > 0 -> childInclination = "Малая наклонность"
            else -> {
                childInclination = "Пока нет данных"
            }
        }

        when(person.child_gender){
            "male" -> imageView.setImageResource(R.drawable.boy)
            "female" -> imageView.setImageResource(R.drawable.girl)
        }
        textView.text = person.name
        textView1.text = childInclination

        return view
    }

    override fun getItem(position: Int): Children? {
        return items[position]
    }
}