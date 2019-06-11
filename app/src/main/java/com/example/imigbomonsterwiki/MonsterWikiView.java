package com.example.imigbomonsterwiki;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MonsterWikiView {
    private static final int kStrokeAmount = 5;
    private static Context appContext;
    private MonsterFilter monsterFilter = new MonsterFilter(this);
    private LinearLayout view;
    private TableLayout monsterTable;
    private TextView showMoreTextView;

    public MonsterWikiView(Context context){
        appContext = context;
        view = new LinearLayout(appContext);
        view.setOrientation(LinearLayout.VERTICAL);

        addRarityFilter(view);
        addElementsFilter(view);

        monsterTable = new TableLayout(appContext);
        view.addView(monsterTable);

        showMoreTextView = new TextView(appContext);
        SpannableString text = new SpannableString("Ver más");
        text.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        showMoreTextView.setText(text);
        showMoreTextView.setTextSize(20);
        showMoreTextView.setPadding(5,20,5,10);
        showMoreTextView.setTextColor(Color.BLUE);
        showMoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        showMoreTextView.setClickable(true);
        showMoreTextView.setOnClickListener(onShowMoreTextViewClick);

        view.addView(showMoreTextView);
    }

    public LinearLayout getView(){
        return view;
    }

    private void addRarityFilter(LinearLayout container){
        HorizontalScrollView rarityScrollView = new HorizontalScrollView(appContext);
        LinearLayout rarityLayout = new LinearLayout(appContext);
        rarityLayout.setOrientation(LinearLayout.HORIZONTAL);

        for(byte i=1; i <= kStrokeAmount; i++){
            ImageView stroke = new ImageView(appContext);
            stroke.setImageDrawable(appContext.getDrawable(MonsterFilter.getRarityStroke(i)));
            stroke.setPadding(10,10,10,10);
            stroke.setMinimumHeight(150);
            stroke.setMinimumWidth(150);
            stroke.setOnClickListener(onRaritySelected);
            stroke.setId(i);

            rarityLayout.addView(stroke);
        }

        rarityScrollView.addView(rarityLayout);
        container.addView(rarityScrollView);
    }

    private void addElementsFilter(LinearLayout container){
        HorizontalScrollView elementsScrollView = new HorizontalScrollView(appContext);
        LinearLayout elementsLayout = new LinearLayout(appContext);
        elementsLayout.setOrientation(LinearLayout.HORIZONTAL);

        for(byte i=0; i < MonsterFilter.elementsList.length; i++){
            ImageView elementIcon = new ImageView(appContext);
            elementIcon.setImageDrawable(appContext.getDrawable(MonsterFilter.getElementIcon(MonsterFilter.elementsList[i])));
            elementIcon.setPadding(10, 10, 0, 20);
            elementIcon.setMinimumWidth(150);
            elementIcon.setMinimumHeight(150);
            elementIcon.setOnClickListener(onElementSelected);
            elementIcon.setId(i);

            elementsLayout.addView(elementIcon);
        }

        elementsScrollView.addView(elementsLayout);
        container.addView(elementsScrollView);
    }

    public void updateMonsterTableView(ArrayList<MonsterDisplay> monstersList, boolean resetTable){
        if(resetTable){
            monsterTable.removeAllViews();
        }
        int i = 0;

        while(i < monstersList.size()){
            byte rowElementsCont = 0;
            TableRow monstersRow = new TableRow(appContext);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
            layoutParams.height = TableRow.LayoutParams.WRAP_CONTENT;
            layoutParams.width = TableRow.LayoutParams.MATCH_PARENT;
            monstersRow.setLayoutParams(layoutParams);
            if (i/4 % 2 == 0) {
                monstersRow.setBackgroundColor(Color.LTGRAY);
            } else {
                monstersRow.setBackgroundColor(Color.WHITE);
            }

            while(rowElementsCont < 4 && i < monstersList.size()){
                ImageView monsterImageView = new ImageView(appContext);
                getMonsterImage(monstersList.get(i).getImageKey(), (byte) 1, monsterImageView);
                monsterImageView.setPadding(5,5,5,5);
                monsterImageView.setClickable(true);
                monsterImageView.setOnClickListener(onMonsterImageClickListener);
                monsterImageView.setId(monstersList.get(i).getId());

                monstersRow.addView(monsterImageView);
                rowElementsCont++;
                i++;
            }

            monsterTable.addView(monstersRow);
        }
    }

    private View.OnClickListener onMonsterImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MonsterDatabase monsterDatabase = new MonsterDatabase(appContext, false);
            Monster monster = monsterDatabase.getMonsterByID(v.getId());
            Intent monsterInfoViewIntent = new Intent(appContext, MonsterInfoActivity.class);
            monsterInfoViewIntent.putExtra("monsterInfo", monster);
            monsterInfoViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(monsterInfoViewIntent);
        }
    };

    private View.OnClickListener onRaritySelected = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monsterFilter.onRaritySelected((ImageView)v, appContext);
        }
    };

    private View.OnClickListener onElementSelected = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            monsterFilter.onElementSelected((ImageView)v, appContext);
        }
    };

    private View.OnClickListener onShowMoreTextViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            monsterFilter.onShowMoreClicked(appContext);
        }
    };

    protected void setShowMoreTextViewVisible(boolean visible){
        if (visible) {
            showMoreTextView.setVisibility(View.VISIBLE);
        } else {
            showMoreTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void getMonsterImage(String imageKey, byte monsterStage, final ImageView imageView){
        new ImageParser(imageKey, monsterStage, imageView);
    }

}
