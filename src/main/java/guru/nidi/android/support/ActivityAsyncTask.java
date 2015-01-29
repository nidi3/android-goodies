package guru.nidi.android.support;

import android.app.Activity;
import android.os.AsyncTask;

/**
 *
 */
public abstract class ActivityAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {
    protected final Activity activity;

    protected ActivityAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        activity.setProgressBarVisibility(true);
    }

    @Override
    protected void onPostExecute(Result result) {
        afterExecute(result);
        activity.setProgressBarVisibility(false);
    }

    protected abstract void afterExecute(Result result);
}
