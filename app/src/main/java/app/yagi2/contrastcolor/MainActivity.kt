package app.yagi2.contrastcolor

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val listAdapter = ColorItemListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listAdapter
        }

        @ColorInt
        val colors = mutableListOf<Int>()
        (0 until 256 step 31).forEach { r ->
            (0 until 256 step 31).forEach { g ->
                (0 until 256 step 31).forEach { b ->
                    colors.add(Color.rgb(r, g, b))
                }
            }
        }
        listAdapter.items = colors
    }
}

class ColorItemListAdapter : RecyclerView.Adapter<ColorItemListAdapter.ItemViewHolder>() {

    @ColorInt
    var items: List<Int> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun create(parent: ViewGroup): ItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.listitem_color, parent, false)
                return ItemViewHolder(view)
            }
        }

        fun bind(@ColorInt item: Int) {
            view.rootView.setBackgroundColor(item)
            view.findViewById<TextView>(R.id.text).setTextColor(calcTextColor(item))
        }

        @ColorInt
        private fun calcTextColor(@ColorInt backGroundColor: Int): Int {
            val wc = ColorUtils.calculateContrast(Color.WHITE, backGroundColor)
            val bc = ColorUtils.calculateContrast(Color.BLACK, backGroundColor)

            return if (wc > bc) Color.WHITE else Color.BLACK
        }
    }
}