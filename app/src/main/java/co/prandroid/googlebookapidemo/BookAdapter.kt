package co.prandroid.googlebookapidemo;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


/**
 * Created by dharma on 19/06/17.
 */

class BookAdapter(context: Context) : ArrayAdapter<Book>(context, 0) {

    override fun getView(position: Int, currentView: View?, parent: ViewGroup): View {
        var currentView = currentView
        if (currentView == null) {
            currentView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val currentVolume = getItem(position)

        val title = currentView?.findViewById(R.id.title) as TextView
        val author = currentView.findViewById(R.id.author) as TextView
        val image = currentView.findViewById(R.id.image) as ImageView

        title.text = currentVolume!!.title
        author.text = currentVolume.author
        Glide.with(context)
                .load(currentVolume.imageUrl)
                .into(image)
        return currentView
    }
}