package co.prandroid.googlebookapidemo

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

/**
 * Created by dharma on 19/06/17.
 */
val defualtImageUrl="https://cdn3.iconfinder.com/data/icons/document-icons-2/30/647703-image-128.png"

// extract the data from json
fun extractDataFromJSON(resources: Resources, jsonResponse: String?): List<Book>? {
    val volumes = ArrayList<Book>()
    try {
        val rootObject = JSONObject(jsonResponse)
        val items = rootObject.getJSONArray("items")
        for (i in 0..items.length() - 1) {


            /** Extracts the data from the json response for that item on the items array*/
            val volume = items.getJSONObject(i)
            val volumeInfo = volume.getJSONObject("volumeInfo")
            val volumeTitle = volumeInfo.getString("title")
            val authorName = volumeInfo.getJSONArray("authors").getString(0)
            val infoLink = volumeInfo.getString("canonicalVolumeLink")


            /** makes an http request for the image of the book and parse it into bitmap */
            var image : String
            if(volumeInfo.has("imageLinks")) {
                image = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail")
            }
            else {
                image= defualtImageUrl
            }
            volumes.add(Book(authorName, volumeTitle, image, infoLink))
        }
    } catch (e: JSONException) {
        Log.e("QueryUtils", "Error during parsing the JSON response from google book api", e)
    }
    return volumes
}


// create url from string
fun createURL(stringURL: String?): URL? {
    if (stringURL == null) {
        return null
    }
    var url: URL? = null
    try {
        url = URL(stringURL)
    } catch (e: MalformedURLException) {
        Log.e("QueryUtils", "Error forming the url.", e)
    }
    return url
}

// read fil from input stream
fun readFromInputStream(stream: InputStream?): String? {
    if (stream == null) {
        return null
    }
    val streamReader = InputStreamReader(stream)
    val bufferedReader = BufferedReader(streamReader)
    val response = StringBuilder()
    var tempString: String?
    try {
        tempString = bufferedReader.readLine()
        while (tempString != null) {
            response.append(tempString)
            tempString = bufferedReader.readLine()
        }
    } catch (e: IOException) {
        Log.e("QueryUtils", "Error reading from inputStream", e)
    }
    return response.toString()
}

fun makeHttpRequest(stringURL: String?): InputStream? {
    if (stringURL == null) {
        return null
    }
    val url = createURL(stringURL) ?: return null

    var urlConnection: HttpURLConnection?
    var inputStream: InputStream? = null
    try {
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.connectTimeout = 5000
        urlConnection.readTimeout = 5000
        urlConnection.connect()
        val responseCode = urlConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = urlConnection.inputStream
        } else {
            Log.e("QueryUtils", "Server error, code: " + responseCode)
        }
    } catch (e: IOException) {
        Log.e("QueryUtils", "Error opening url Connection.", e)
    }
    return inputStream
}