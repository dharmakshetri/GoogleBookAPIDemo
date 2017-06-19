package co.prandroid.googlebookapidemo;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.Resources;

import java.util.List;

import static co.prandroid.googlebookapidemo.DataUtilsKt.extractDataFromJSON;
import static co.prandroid.googlebookapidemo.DataUtilsKt.makeHttpRequest;
import static co.prandroid.googlebookapidemo.DataUtilsKt.readFromInputStream;


/**
 * Created by dharma on 19/06/17.
 */

public class Utils extends AsyncTaskLoader<List<Book>> {
    private String url;
    private Resources resources = null;

    public Utils(String url, Resources resources, Context context) {
        super(context);
        this.url = url;
        this.resources = resources;
    }

    @Override
    public List<Book> loadInBackground() {
        if (url == null) {
            return null;
        }
        return extractDataFromJSON(resources, readFromInputStream(makeHttpRequest(url)));
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
