package co.prandroid.googlebookapidemo

import org.junit.After
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val urlString="https://www.googleapis.com/books/v1/volumes?q=android"
    @Test
    fun valid_CreateBook_Url() {
        val url=createURL("https://www.googleapis.com/books/v1/volumes?q="+"android")
        val tempUrl=createURL(urlString)
        assertEquals(tempUrl,url)
    }

    @Test
    fun makeHttpRequest_NotNull(){
        assertNotNull("string url is null", makeHttpRequest(urlString))
    }

}
