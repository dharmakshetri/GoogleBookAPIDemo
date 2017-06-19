package co.prandroid.googlebookapidemo

import android.app.Activity
import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by dharma on 19/06/17.
 */
class MainActivity : Activity(), LoaderManager.LoaderCallbacks<List<Book>> {

    private val booksURL = "https://www.googleapis.com/books/v1/volumes?q="
    private var adapter: BookAdapter? = null
    private val LOADER_ID = 1

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loaderManager = loaderManager
        val o = loaderManager.getLoader<Any>(LOADER_ID)
        if (o != null) {
            getLoaderManager().initLoader(LOADER_ID, null, this)
        }

        loading!!.visibility = View.GONE
        grid_view.emptyView = empty_state_view
        adapter = BookAdapter(this)
        grid_view.adapter = adapter

        grid_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val currentVolume = adapter!!.getItem(position)
            val webPageUri = Uri.parse(currentVolume!!.infoLink)
            val webIntent = Intent(Intent.ACTION_VIEW, webPageUri)
            startActivity(webIntent)
        }

        search_button.setOnClickListener { v ->
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                if (search_keys!!.text.length > 0) {
                    loading!!.visibility = View.VISIBLE
                    getLoaderManager().restartLoader(LOADER_ID,null,this@MainActivity)
                } else {
                    Toast.makeText(v.context, "You have to provide a book name to search.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(v.context, "No internet connection available.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): AsyncTaskLoader<List<Book>> {
        val query = StringBuilder(booksURL)
        val keys = search_keys!!.text.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (str in keys) {
            query.append(str)
        }
        val loader = Utils(query.toString(), resources, this)
        return loader
    }

    override fun onLoadFinished(loader: Loader<List<Book>>, volumes: List<Book>?) {
        loading!!.visibility = View.GONE
        adapter!!.clear()
        if (volumes != null) {
            adapter!!.addAll(volumes)
        }
    }

    override fun onLoaderReset(loader: Loader<List<Book>>) {
        adapter!!.clear()
    }
}

