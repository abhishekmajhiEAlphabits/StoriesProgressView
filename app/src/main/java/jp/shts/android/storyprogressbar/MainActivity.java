package jp.shts.android.storyprogressbar;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class MainActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private static final int PROGRESS_COUNT = 6;

    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private VideoView video;

    private int counter = 0;
    //    String file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/sample.mp4";
    String path = "android.resource://jp.shts.android.storyprogressbar/raw/sample";

    private Slide[] slides = {new Slide(0, path),
            new Slide(R.drawable.sportscar, "0"),
            new Slide(0, path),
            new Slide(R.drawable.sample2, "0"),
            new Slide(0, path),
            new Slide(R.drawable.sportscar, "0")};

    private final int[] resources = new int[]{
            R.drawable.sportscar,
            R.drawable.sample2,
            R.drawable.sportscar,
            R.drawable.sample2,
            R.drawable.sportscar,
            R.drawable.sample2,

    };

    private final long[] durations = new long[]{
            6000, 3000, 4000, 7000, 6000, 9000,
    };

    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
//        storiesProgressView.setStoryDuration(3000L);
        // or
        storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView.setStoriesListener(this);
//        storiesProgressView.startStories();
        counter = 0;
        storiesProgressView.startStories(counter);

        image = (ImageView) findViewById(R.id.image);
        video = (VideoView) findViewById(R.id.video);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) video.getLayoutParams();
//        params.width = metrics.widthPixels;
//        params.height = metrics.heightPixels;
//        params.leftMargin = 0;
//        video.setLayoutParams(params);

        if (slides[counter].imgRes == 0) {
            Uri videoUri = Uri.parse(slides[0].videoRes);
            video.setVideoURI(videoUri);
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
//                    mediaPlayer.start();
                    video.start();
                }
            });
        } else {
            image.setImageResource(slides[0].imgRes);
        }
//        image.setImageResource(resources[counter]);

        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onNext() {
//        Log.d("abhi", resources[counter]);
//        image.setImageResource(resources[++counter]);
        playSlideShow();
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
//        image.setImageResource(resources[--counter]);
        reverseSlideShow();
    }

    @Override
    public void onComplete() {
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }

    private File createImageFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        Log.d("abhi", "dir : " + storageDir);
        return File.createTempFile(
                "Chromium_Demo",
                ".mp4",
                storageDir
        );
    }

    private Uri getFileUri() {
        File photoFile = null;
        Uri mCurrentPhotoUri = null;
        try {
//            photoFile = createImageFile();
//            photoFile = new File(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            mCurrentPhotoUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    photoFile
            );
        }
        Log.d("abhi", "photoUri : " + mCurrentPhotoUri);
        return mCurrentPhotoUri;
    }

    private void playSlideShow() {
        if (slides[++counter].imgRes == 0) {
            try {
                image.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
//                getFileUri();
//                URL url = new URL(slides[0].videoRes);
//                Uri videoUri = Uri.parse(url.toURI().toString());
                Uri videoUri = Uri.parse(slides[counter].videoRes);
                Log.d("abhi", "uri : " + videoUri);
                video.setVideoURI(videoUri);
                video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
//                        mediaPlayer.start();
                        video.start();
                    }
                });
            } catch (Exception e) {
                Log.d("abhi", e.toString());
            }
        } else {
            video.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            image.setImageResource(slides[counter].imgRes);
            Log.d("abhi", "imgRes : " + slides[counter].imgRes);
        }
    }

    private void reverseSlideShow() {
        if (slides[--counter].imgRes == 0) {
            try {
                image.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
//                getFileUri();
//                URL url = new URL(slides[0].videoRes);
//                Uri videoUri = Uri.parse(url.toURI().toString());
                Uri videoUri = Uri.parse(slides[counter].videoRes);
                Log.d("abhi", "uri : " + videoUri);
                video.setVideoURI(videoUri);
                video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
//                        mediaPlayer.start();
                        video.start();
                    }
                });
            } catch (Exception e) {
                Log.d("abhi", e.toString());
            }
        } else {
            video.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            image.setImageResource(slides[counter].imgRes);
            Log.d("abhi", "imgRes : " + slides[counter].imgRes);
        }
    }
}
