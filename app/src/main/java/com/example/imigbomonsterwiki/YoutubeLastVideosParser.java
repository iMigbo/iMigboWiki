package com.example.imigbomonsterwiki;

import android.os.AsyncTask;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class YoutubeLastVideosParser extends AsyncTask <YoutubeVideo, Object, Object>{

    private static final String kYoutubeCredentialKey = "AIzaSyCGAMpsuhtzTQepesUrzZ1FZlmpRCfdnKw";
    private static final String kiMigboGamesChannelID = "UCkzef9u0H9rQbgScH37zTkA";
    private static final String kActivitiesURL = "https://www.googleapis.com/youtube/v3/activities?part=snippet,contentDetails&channelId="+kiMigboGamesChannelID+"&key="+kYoutubeCredentialKey;

    @Override
    protected Object doInBackground(YoutubeVideo... video) {
        try {
            final InputStream is = new URL(kActivitiesURL).openConnection().getInputStream();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));

            String line = bufferedReader.readLine();
            String jsonFile = "";
            while(line != null){
                jsonFile += line;
                line = bufferedReader.readLine();
            }

            JSONObject youtubeVideos = new JSONObject(jsonFile);
            final JSONArray pageVideos = youtubeVideos.getJSONArray("items");

            int i = 0;

            while(!((JSONObject) pageVideos.get(i)).getJSONObject("snippet").getString("type").equals("upload")){
                 i++;
            }

            video[i].setTitle(((JSONObject) pageVideos.get(i)).getJSONObject("snippet").getString("title"));
            final String videoKey = ((JSONObject) pageVideos.get(i)).getJSONObject("contentDetails").getJSONObject("upload").getString("videoId");
            video[i].setKey(videoKey);

            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o){

    }
}

