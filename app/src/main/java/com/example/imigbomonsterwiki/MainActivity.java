package com.example.imigbomonsterwiki;

import android.content.Context;
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

    private Context context;

    private TextView mTextMessage;
    private LinearLayout linearLayout;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;
    private MonsterDatabase monsterDatabase;
    private MonsterWikiView monsterWikiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        linearLayout = findViewById(R.id.linearLayout);
        mTextMessage = findViewById(R.id.message);

        //Initialize Youtube View:
        final String YOUTUBE_CODE = "AIzaSyCGAMpsuhtzTQepesUrzZ1FZlmpRCfdnKw";
        youTubePlayerView = new YouTubePlayerView(this);
        youTubePlayerView.initialize(YOUTUBE_CODE, MainActivity.this);
        initialize(YOUTUBE_CODE,this);

        //Initialize monster database:
        monsterDatabase = new MonsterDatabase(this,false);
        monsterDatabase.parseMonstersJSON();
        monsterWikiView = new MonsterWikiView(context);

        //Default Layout:
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            cleanLayout();
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    setYoutubeLayout();
                    return true;
                case R.id.navigation_monsters:
                    setMonsterWikiLayout();
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

    private void setYoutubeLayout(){
        mTextMessage.setText(R.string.title_news);
        youTubePlayer.cueVideo(lastVideoCode);
        linearLayout.addView(youTubePlayerView);
    }

    private void setMonsterWikiLayout(){
        mTextMessage.setText(R.string.title_monsters);
        linearLayout.addView(monsterWikiView.getView());
        monsterWikiView.updateMonsterTableView(monsterDatabase.getMonstersDisplayOrderByRelease(0,20), true);
    }

    private void getLastiMigboVideo()
    {
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(lastVideoCode);
        this.youTubePlayer = youTubePlayer;
        setYoutubeLayout();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void initialize(String s, YouTubePlayer.OnInitializedListener onInitializedListener) {

    }
}
