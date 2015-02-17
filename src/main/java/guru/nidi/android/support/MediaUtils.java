package guru.nidi.android.support;

import android.media.MediaPlayer;

import static guru.nidi.android.ApplicationContextHolder.context;

/**
 *
 */
public class MediaUtils {
    private MediaUtils(){}

    private static final MediaPlayer.OnCompletionListener RELEASE_ON_COMPLETION = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
        }
    };

    public static void playSound(int resId) {
        final MediaPlayer mp = MediaPlayer.create(context(), resId);
        mp.setOnCompletionListener(RELEASE_ON_COMPLETION);
        mp.start();
    }
}
