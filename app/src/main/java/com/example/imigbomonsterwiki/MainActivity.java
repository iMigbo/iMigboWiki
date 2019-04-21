package com.example.imigbomonsterwiki;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.Provider{

    private String lastVideoCode = "UIvNqlj-U2I";

    private TextView mTextMessage;
    private LinearLayout linearLayout;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linearLayout);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_monsters);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        youTubePlayerView = new YouTubePlayerView(this);
        final String YOUTUBE_CODE = "AIzaSyCGAMpsuhtzTQepesUrzZ1FZlmpRCfdnKw";
        youTubePlayerView.initialize(YOUTUBE_CODE, MainActivity.this);
        initialize(YOUTUBE_CODE,this);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            cleanLayout();
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    mTextMessage.setText(R.string.title_news);
                    youTubePlayer.cueVideo(lastVideoCode);
                    linearLayout.addView(youTubePlayerView);
                    return true;
                case R.id.navigation_monsters:
                    mTextMessage.setText(R.string.title_monsters);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.title_settings);
                    return true;
            }
            return false;
        }
    };

    private void cleanLayout()
    {
        youTubePlayer.pause();
        linearLayout.removeAllViews();
    }

    private void getLastiMigboVideo()
    {
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(lastVideoCode);
        this.youTubePlayer = youTubePlayer;
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void initialize(String s, YouTubePlayer.OnInitializedListener onInitializedListener) {

    }
}
