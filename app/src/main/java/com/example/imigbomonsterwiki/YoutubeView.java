package com.example.imigbomonsterwiki;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeView extends YouTubeBaseActivity{

    private Context appContext;
    private LinearLayout container;

    public YoutubeView(Context context, YoutubeVideo video){
        appContext = context;
        container = new LinearLayout(appContext);
        new YoutubeLastVideosParser().execute(video);
    }

    public LinearLayout getView(){
        return container;
    }
}
