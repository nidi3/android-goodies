package guru.nidi.android.view;

import android.content.DialogInterface;

/**
 *
 */
public class DismissingOnClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
