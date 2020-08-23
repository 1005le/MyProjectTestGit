package com.example.myapplication;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
        SeekBar.OnSeekBarChangeListener, MyAdapter.OnClickItem {
    BottomNavigationView bottomNavigationView;
    BottomNavigationItemView itemView3;

    TextView tvTest;
    TextView textView1;
    TextView tvTimeStart;
    TextView tvTimeEnd;

    //play audio from server
    SeekBar seekBarProgress;
    Button btPlay;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        tvTest = findViewById(R.id.tvReviewDescription);
        textView1 = findViewById(R.id.textView1);

        seekBarProgress = (SeekBar) findViewById(R.id.seekbar);
        btPlay = (Button) findViewById(R.id.bt_play_audio);
        tvTimeStart = (TextView) findViewById(R.id.songCurrentDurationLabel);
        tvTimeEnd = (TextView) findViewById(R.id.songTotalDurationLabel);

        recyclerView = (RecyclerView) findViewById(R.id.rv_lyric);

//        recyclerView.setScrollbarFadingEnabled(false);

        webView = (WebView) findViewById(R.id.webView);
        //showBadge(this,bottomNavigationView, R.id.action_notification, "1");
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);
        itemView3 = (BottomNavigationItemView) menuView.getChildAt(3);

        MenuItem selectedItem = bottomNavigationView.getMenu().findItem(R.id.action_notification);
        // final MenuItem menuItem = menuView.findViewById(R.id.action_notification);

//        View actionView = selectedItem.getActionView();
//        TextView textView = actionView.findViewById(R.id.cart_badge);
//        textView.setText("15");
//        itemView.addView(actionView);
//        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.action_notification);
//        //LayerDrawable icon = (LayerDrawable) item.getIcon();
//
//        BitmapDrawable iconBitmap = (BitmapDrawable)item.getIcon();
//        LayerDrawable iconLayer = new LayerDrawable(new Drawable [] { iconBitmap });
//
//
//        // Update LayerDrawable's BadgeDrawable
//        setBadgeCount(this, iconLayer, 2);


        View notificationBadge = LayoutInflater.from(this).inflate(R.layout.badge_layout, menuView, false);
        AppCompatTextView textView = notificationBadge.findViewById(R.id.cart_badge);
        textView.setText("15");
        itemView.addView(notificationBadge);

        Glide.with(getApplicationContext()).asBitmap().load("").
                apply(new RequestOptions().placeholder(R.drawable.img_no_image).error(R.drawable.img_no_image)).
                into(new CustomTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable profileImage = new BitmapDrawable(getResources(), resource);
                        bottomNavigationView.getMenu().getItem(3).setIcon(profileImage);
                        //itemView3.setIcon(profileImage);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_personal:
                        Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_notification:
                        Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_community:
                        Toast.makeText(MainActivity.this, "Community", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        final String textContent = "Tiếng Việt được chính thức ghi nhận trong Hiến pháp nước " +
                "Cộng hòa xã hội chủ nghĩa Việt Nam 2013, tại Chương I Điều 5 Mục 3 " +
                "";
        tvTest.setText(textContent);
        //We were staying in Boston for a week and after a long day and blah blah blah blah Vinh
//        ViewTreeObserver vto = tvTest.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
////                if(!isEllipsized(tvTest.getText().toString(),textContent)){
////                    //makeTextViewResizable(tvTest, 3, "...See More", true);
////                    textView1.setVisibility(View.VISIBLE);
////                    tvTest.setText(
////                            addClickablePartTextViewResizable(Html.fromHtml(tvTest.getText().toString()), tvTest, 3, "...See More",
////                                    true), TextView.BufferType.SPANNABLE);
////                }else{
////                    textView1.setVisibility(View.GONE);
////                }
//                setLabelAfterEllipsis(tvTest,R.string.see_more,3);
//            }
//        });

        makeTextViewResizable(tvTest, 3, "...See More", true);


//        makeTextViewResizable(tvTest,3,"Xin chào, anh  affffffffffffff ầDDDAaffffsfsafsfffsffffff" +
//                "sffffffffffffffffffffffffffffffff ngot nffffffffffffff á nh vhjhhhjjjjjjjjjjjjjjjjjjjjjjjjjhjjjjjjjjjjjjjjjkhkkkkkkkkkkkkkkkkkkkkkkkkkkk end line",false);
        //makeTextViewResizable(tvTest, 3, "...SeeMore");


        initView();


        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        String url = "https://mindorks.s3.ap-south-1.amazonaws.com/courses/MindOrks_Android_Online_Professional_Course-Syllabus.pdf";
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url");
    }

    private void initView() {

        btPlay.setOnClickListener(this);

        seekBarProgress.setMax(99); // It means 100% .0-99
//        seekBarProgress.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        // Listeners
//        seekBarProgress.setOnSeekBarChangeListener(this); // Important

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<MusicResponse> listMusic = new ArrayList<>();
        listMusic.add(new MusicResponse(1, "loi 1", "00:01", "00:10"));
        listMusic.add(new MusicResponse(2, "loi 2", "00:11", "00:20"));
        listMusic.add(new MusicResponse(3, "loi 3", "00:21", "00:40"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        listMusic.add(new MusicResponse(4, "loi 4", "00:41", "00:59"));
        mAdapter = new MyAdapter(getApplicationContext(), listMusic);
        mAdapter.setOnClickItem(this);
        recyclerView.setAdapter(mAdapter);
    }

    public void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
//            Log.d("VinhLT","curentDuration"+currentDuration);
//            Log.d("VinhLT", "duảtion" + totalDuration);
            // Displaying Total Duration time
            tvTimeEnd.setText("" + Utilites.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            tvTimeStart.setText("" + Utilites.milliSecondsToTimer(currentDuration));
            String currentTime = Utilites.milliSecondsToTimer(currentDuration);
            // Updating progress bar
            int progress = (int) (Utilites.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekBarProgress.setProgress(progress);
            Log.d("VinhLT", "progress" + progress);
            hightlightItem(Utilites.milliSecondsToTimer(currentDuration));
            // Running this thread after 100 milliseconds
            handler.postDelayed(this, 100);
        }
    };

    private void hightlightItem(String currentDuration) {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            MusicResponse musicResponse = (MusicResponse) mAdapter.getmList().get(i);
            if (musicResponse != null && Utilites.formatTimeToDate(musicResponse.getTimeStart()).before(Utilites.formatTimeToDate(currentDuration))
                    && Utilites.formatTimeToDate(musicResponse.getTimeEnd()).after(Utilites.formatTimeToDate(currentDuration))) {
                mAdapter.setPosition_hightLight(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine <= 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                } else if (tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                }
            }
        });

    }

    private void setLabelAfterEllipsis(TextView textView, int labelId, int maxLines) {

        if (textView.getLayout().getEllipsisCount(maxLines - 1) == 0) {
            return; // Nothing to do
        }

        int start = textView.getLayout().getLineStart(0);
        int end = textView.getLayout().getLineEnd(textView.getLineCount() - 1);
        String displayed = textView.getText().toString().substring(start, end);
        int displayedWidth = getTextWidth(displayed, textView.getTextSize());

        String strLabel = textView.getContext().getResources().getString(labelId);
        String ellipsis = "...";
        String suffix = ellipsis + strLabel;

        int textWidth;
        String newText = displayed;
        textWidth = getTextWidth(newText + suffix, textView.getTextSize());

        while (textWidth > displayedWidth) {
            newText = newText.substring(0, newText.length() - 1).trim();
            textWidth = getTextWidth(newText + suffix, textView.getTextSize());
        }

        textView.setText(newText + suffix);
    }

    private int getTextWidth(String text, float textSize) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);

        int width = (int) Math.ceil(bounds.width());
        return width;
    }


    public static boolean isEllipsized(String newValue, String oldValue) {
        return !((newValue).equals(oldValue));
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - 4 - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
//                    tv.setText(
//                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
//                                    viewMore), TextView.BufferType.SPANNABLE);
                }
//                else {
//                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
//                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
//                    tv.setText(text);
//                    tv.setMovementMethod(LinkMovementMethod.getInstance());
//                    tv.setText(
//                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
//                                    viewMore), TextView.BufferType.SPANNABLE);
//                }
            }
        });

    }

//    private static SpannableStringBuilder addClickablePartTextViewResizable(
//            final String strSpanned, final TextView tv, final int maxLine,
//            final String spanableText, final boolean viewMore) {
//        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
//
//        if (strSpanned.contains(spanableText)) {
//            ssb.setSpan(
//                    new ClickableSpan() {
//
//                        @Override
//                        public void onClick(View widget) {
//
//                            if (viewMore) {
//                                tv.setLayoutParams(tv.getLayoutParams());
//                                tv.setText(tv.getTag().toString(),
//                                        TextView.BufferType.SPANNABLE);
//                                tv.invalidate();
//                                makeTextViewResizable(tv, -3, "...Read Less",
//                                        false);
//                                tv.setTextColor(Color.BLACK);
//                            } else {
//                                tv.setLayoutParams(tv.getLayoutParams());
//                                tv.setText(tv.getTag().toString(),
//                                        TextView.BufferType.SPANNABLE);
//                                tv.invalidate();
//                                makeTextViewResizable(tv, 3, "...Read More",
//                                        true);
//                                tv.setTextColor(Color.BLACK);
//                            }
//
//                        }
//                    }, strSpanned.indexOf(spanableText),
//                    strSpanned.indexOf(spanableText) + spanableText.length(), 0);
//
//        }
//        return ssb;
//
//    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
//                    if (viewMore) {
//                        tv.setLayoutParams(tv.getLayoutParams());
//                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
//                        tv.invalidate();
//                        makeTextViewResizable(tv, -1, "See Less", false);
//                    } else {
//                        tv.setLayoutParams(tv.getLayoutParams());
//                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
//                        tv.invalidate();
//                        makeTextViewResizable(tv, 3, ".. See More", true);
//                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }


    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.badge_layout, bottomNavigationView, false);

        //TextView text = badge.findViewById(R.id.badge_text_view);
//        text.setText(value);
//        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        //seekBarProgress.setSecondaryProgress(i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        handler.removeCallbacks(mUpdateTimeTask);
        btPlay.setBackgroundResource(R.drawable.ic_tab_home);
        Log.d("VinhLT","onCompletion"+"currentMedia"+mediaPlayer.getCurrentPosition());
        Log.d("VinhLT","onCompletion"+"duration"+mediaPlayer.getDuration());
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
            seekBarProgress.setProgress(0);
            Log.d("VinhLT","onCompletion"+"Stop");
//            seekBarProgress.setMax(100);
//        }
//        seekBarProgress.setMax(0);
//        updateProgressBar();
     /*   try {
//                mediaPlayer.setDataSource("https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3"); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
            mediaPlayer.setDataSource("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3"); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
            mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            seekBarProgress.setOnSeekBarChangeListener(this); // Important
            btPlay.setBackgroundResource(R.drawable.ic_tab_home);
        } else {
            mediaPlayer.pause();
            btPlay.setBackgroundResource(R.drawable.ic_tab_publish);
        }

//            primarySeekBarProgressUpdater();

        // set Progress bar values
            seekBarProgress.setProgress(0);
//            seekBarProgress.setMax(100);

        // Updating progress bar
        updateProgressBar();*/
//        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//            seekBarProgress.setProgress(seekBarProgress.getMax());
////            mediaPlayer.reset();
//        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_play_audio) {
            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            try {
//                mediaPlayer.setDataSource("https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3"); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer.setDataSource("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3"); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                seekBarProgress.setOnSeekBarChangeListener(this); // Important
//                seekBarProgress.setProgress(0);
//                seekBarProgress.setMax(mediaPlayer.getDuration());
                btPlay.setBackgroundResource(R.drawable.ic_tab_home);
            } else {
                mediaPlayer.pause();
                btPlay.setBackgroundResource(R.drawable.ic_tab_publish);
            }

//            primarySeekBarProgressUpdater();

            // set Progress bar values
//            seekBarProgress.setProgress(0);
//            seekBarProgress.setMax(100);

            // Updating progress bar
            updateProgressBar();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        /*if(view.getId() == R.id.seekbar){
         *//** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*//*
            if(mediaPlayer.isPlaying()){
                SeekBar sb = (SeekBar)view;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }*/
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//            MainActivity.this.seekBarProgress.setProgress(0);
//        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
//        mediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
//        mediaPlayer.start();
//        seekBar.setProgress(0);
//        seekBar.setMax(mediaPlayer.getDuration());
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = Utilites.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);
        Log.d("VinhLT", "handle onTouch SeekBar");

        // update timer progress again
        updateProgressBar();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClicktem(int position, MusicResponse musicResponse) {
        //convert time to milisecond
//        Time t = Time.valueOf(musicResponse.getTimeStart());
//        String time= musicResponse.getTimeStart();
//        LocalTime localTime = LocalTime.parse(time);
//        int minisecond = localTime.toSecondOfDay() * 1000;

//        String myDate = "2018/11/13 12:00:00";
//        String myDate = musicResponse.getTimeStart();
//        LocalDateTime localDateTime = LocalDateTime.parse(myDate, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss") );
//        LocalDateTime localDateTime = LocalDateTime.parse(myDate, DateTimeFormatter.ofPattern("mm:ss") );
//        int minisecond= localDateTime.toLocalTime().toSecondOfDay() * 1000;

        //long minisecond = milliseconds(musicResponse.getTimeStart());

        String[] tokens = musicResponse.getTimeStart().split(":");
        int secondsToMs = Integer.parseInt(tokens[1]) * 1000;
        int minutesToMs = Integer.parseInt(tokens[0]) * 60000;
        int hoursToMs = Integer.parseInt(tokens[0]) * 3600000;
        long minisecond = secondsToMs + minutesToMs + hoursToMs;
        Log.d("VinhLT:", "timeStar" + musicResponse.getTimeStart());
        Log.d("VinhLT:", "minisecond" + minisecond);
        // Updating progress bar
        int progress = (int) (Utilites.getProgressPercentage(minisecond, 132206));
        //Log.d("Progress", ""+progress);
        seekBarProgress.setProgress(progress);
        //hightlightItem(Utilites.milliSecondsToTimer(minisecond));
//        // Running this thread after 100 milliseconds
//        handler.postDelayed(this, 100);
        int currentPosition = Utilites.progressToTimer(seekBarProgress.getProgress(), 132206);
        Log.d("VinhLT:", "current_position" + currentPosition);
/*        if (mediaPlayer != null) {
//            mediaPlayer.seekTo((int) minisecond);
//            mAdapter.setPosition_hightLight(position);
//            int currentPosition = Utilites.progressToTimer(seekBarProgress.getProgress(), 132206);

            // forward or backward to certain seconds
            try {
//                mediaPlayer.setDataSource("https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3"); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer.setDataSource("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3"); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                seekBarProgress.setOnSeekBarChangeListener(this); // Important
                btPlay.setBackgroundResource(R.drawable.ic_tab_home);
            }*/

        mediaPlayer.seekTo(currentPosition);
        updateProgressBar();
        Log.d("VinhLT:", "update item");
//        }
    }

    public long milliseconds(String date) {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }
}
