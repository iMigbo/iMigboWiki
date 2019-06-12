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

    private final String kDefaultVideoKey = "UIvNqlj-U2I";
    private TextView mTextMessage;
    private LinearLayout linearLayout;
    private MonsterDatabase monsterDatabase;
    private MonsterWikiView monsterWikiView;
    private YoutubeVideoData lastVideo = new YoutubeVideoData(kDefaultVideoKey);
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
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linearLayout);
        mTextMessage = findViewById(R.id.message);

        //Initialize Youtube View:
        new YoutubeLastVideoParser().execute(lastVideo);

        //Initialize monster database:
        monsterDatabase = new MonsterDatabase(this,false);
        monsterDatabase.parseMonstersJSON();
        monsterWikiView = new MonsterWikiView(this);

        //Default Layout:
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setYoutubeLayout();
    }

    private void cleanLayout()
    {
        linearLayout.removeAllViews();
    }

    private void setYoutubeLayout(){
        mTextMessage.setText(R.string.title_news);

        final TextView videoTitleTextView = new TextView(this);
        videoTitleTextView.setText(lastVideo.getTitle());
        videoTitleTextView.setTextSize(20);
        videoTitleTextView.setPadding(5,10,5,20);

        YouTubePlayerView youTubePlayer = new YouTubePlayerView(this);
        youTubePlayer.initialize(lastVideo.getKey(), this);

        final TextView videoDescription = new TextView(this);
        videoDescription.setText(lastVideo.getDescription());
        videoDescription.setTextSize(18);
        videoDescription.setPadding(5, 20, 5, 10);

        linearLayout.addView(videoTitleTextView);
        linearLayout.addView(youTubePlayer);
        linearLayout.addView(videoDescription);
    }

    private void setMonsterWikiLayout(){
        mTextMessage.setText(R.string.title_monsters);
        linearLayout.addView(monsterWikiView.getView());
        monsterWikiView.updateMonsterTableView(monsterDatabase.getMonstersDisplayOrderByRelease(0,20), true);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(lastVideo.getKey());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void initialize(String s, YouTubePlayer.OnInitializedListener onInitializedListener) {

    }
}
