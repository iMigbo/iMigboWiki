package com.example.imigbomonsterwiki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageParser extends AsyncTask<Object, Bitmap, Bitmap> {

    ImageView imageView;
    final private String kImageStartURL = "http://mci-static-s1.socialpointgames.com/static/monstercity/mobile/ui/monsters/ui_";
    final private static String kImageEndURL = "@2x.png";

    public ImageParser(String imageKey, byte monsterStage, final ImageView imageView) {
        execute(kImageStartURL + imageKey + "_" + monsterStage, imageView);
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        Bitmap image = null;
        imageView = (ImageView) params[1];
        byte version = 1;
        boolean imageCorrect = false;

        do{
            try {
                final InputStream is = new URL(String.valueOf(params[0])+"_v"+version+ kImageEndURL).openStream();
                image = BitmapFactory.decodeStream(is);
                imageCorrect = true;
                is.close();
            } catch(FileNotFoundException fnfe) {
                version++;
            } catch(IOException e) {
                e.printStackTrace();
            }
        }while(!imageCorrect && version < 5);

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap image){
        imageView.setImageBitmap(image);
    }
}