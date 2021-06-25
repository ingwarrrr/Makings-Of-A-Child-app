package com.example.themakingsofachild
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ChildProhileAdapter(var mCtx: Context, var resource:Int, var items:List<ChildProfileActivity.Inclinations>)
    : ArrayAdapter<ChildProfileActivity.Inclinations>( mCtx , resource , items ){

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var result: Double = -1.0
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val childInclination: String

        val view : View = layoutInflater.inflate(resource , null )
        val textView : TextView = view.findViewById(R.id.titleTv)
        val textView1 : TextView = view.findViewById(R.id.descTv)
        val imageView : ImageView = view.findViewById(R.id.iconIv)
        val inclination : ChildProfileActivity.Inclinations = items[position]

        if (inclination.inclinationResults != "Пока данных нет") {
            val incResult: List<Int> = inclination.inclinationResults
                .split(",")
                .map { it.toInt() }
            result = incResult.sum().toDouble() / incResult.size
        }

        when {
            inclination.inclinationName == "Пока данных нет" -> childInclination = inclination.inclinationName
            result > 0.7 -> childInclination = "Высокая наклонность"
            result > 0.3 -> childInclination = "Средняя наклонность"
            result > 0 -> childInclination = "Малая наклонность"
            else -> {
                childInclination = "Пока нет данных"
            }
        }

        when(position){
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

        textView.text = inclination.inclinationName
        textView1.text = childInclination

        return view
    }

    override fun getItem(position: Int): ChildProfileActivity.Inclinations? {
        return items[position]
    }
}