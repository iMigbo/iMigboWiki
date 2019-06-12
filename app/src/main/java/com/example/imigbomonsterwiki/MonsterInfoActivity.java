package com.example.imigbomonsterwiki;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MonsterInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_info);
        final MonsterData monsterData = (MonsterData) getIntent().getSerializableExtra("monsterInfo");
        ((TextView)findViewById(R.id.monsterNameTextView)).setText(monsterData.getName());

        //MonsterData image:
        ImageView monsterImage = new ImageView(getApplicationContext());
        monsterImage.setMinimumHeight(500);
        monsterImage.setMinimumWidth(500);
        monsterImage.setPadding(0,30,0,0);
        new ImageParser(monsterData.getImageKey(), (byte) 3, monsterImage);

        //Power Stat:
        ImageView powerImage = new ImageView(getApplicationContext());
        powerImage.setImageDrawable(getDrawable(R.drawable.stat_power));
        powerImage.setMinimumWidth(100);
        powerImage.setMinimumHeight(100);

        TextView powerTextView = new TextView(getApplicationContext());
        powerTextView.setText("POWER");
        powerTextView.setTypeface(null, Typeface.BOLD);

        TextView powerAmount = new TextView(getApplicationContext());
        powerAmount.setText(String.valueOf(monsterData.getStrength()));

        final TableRow powerRow = new TableRow(getApplicationContext());
        powerRow.addView(powerImage);
        powerRow.addView(powerTextView);
        powerRow.addView(powerAmount);
        powerRow.setVerticalGravity(Gravity.CENTER);

        //Life Stat:
        ImageView lifeImage = new ImageView(getApplicationContext());
        lifeImage.setImageDrawable(getDrawable(R.drawable.stat_life));
        lifeImage.setMinimumWidth(100);
        lifeImage.setMinimumHeight(100);

        TextView lifeTextView = new TextView(getApplicationContext());
        lifeTextView.setText("LIFE");
        lifeTextView.setTypeface(null, Typeface.BOLD);

        TextView lifeAmount = new TextView(getApplicationContext());
        lifeAmount.setText(String.valueOf(monsterData.getLife()));

        final TableRow lifeRow = new TableRow(getApplicationContext());
        lifeRow.addView(lifeImage);
        lifeRow.addView(lifeTextView);
        lifeRow.addView(lifeAmount);
        lifeRow.setVerticalGravity(Gravity.CENTER);

        //Speed Stat:
        ImageView speedImage = new ImageView(getApplicationContext());
        speedImage.setImageDrawable(getDrawable(R.drawable.stat_speed));
        speedImage.setMinimumWidth(100);
        speedImage.setMinimumHeight(100);

        TextView speedTextView = new TextView(getApplicationContext());
        speedTextView.setText("SPEED");
        speedTextView.setTypeface(null, Typeface.BOLD);

        TextView speedAmount = new TextView(getApplicationContext());
        speedAmount.setText(String.valueOf(monsterData.getSpeed()));

        final TableRow speedRow = new TableRow(getApplicationContext());
        speedRow.addView(speedImage);
        speedRow.addView(speedTextView);
        speedRow.addView(speedAmount);
        speedRow.setVerticalGravity(Gravity.CENTER);

        //Stamina Stat:
        ImageView staminaImage = new ImageView(getApplicationContext());
        staminaImage.setImageDrawable(getDrawable(R.drawable.stat_stamina));
        staminaImage.setMinimumWidth(100);
        staminaImage.setMinimumHeight(100);

        TextView staminaTextView = new TextView(getApplicationContext());
        staminaTextView.setText("STAMINA");
        staminaTextView.setTypeface(null, Typeface.BOLD);

        TextView staminaAmount = new TextView(getApplicationContext());
        staminaAmount.setText(String.valueOf(monsterData.getStamina()));

        final TableRow staminaRow = new TableRow(getApplicationContext());
        staminaRow.addView(staminaImage);
        staminaRow.addView(staminaTextView);
        staminaRow.addView(staminaAmount);
        staminaRow.setVerticalGravity(Gravity.CENTER);

        final TableLayout statsLayout = new TableLayout(getApplicationContext());
        statsLayout.addView(powerRow);
        statsLayout.addView(lifeRow);
        statsLayout.addView(speedRow);
        statsLayout.addView(staminaRow);
        statsLayout.setStretchAllColumns(true);
        statsLayout.setPadding(20, 80, 20, 50);

        //Combat Role:
        final TextView combatRoleTextView = new TextView(getApplicationContext());
        combatRoleTextView.setText("ROL: "+ monsterData.getCombatRole());
        combatRoleTextView.setTextSize(20);
        combatRoleTextView.setTypeface(null, Typeface.BOLD);
        combatRoleTextView.setGravity(Gravity.CENTER);

        //Category:
        final TextView categoryTextView = new TextView(getApplicationContext());
        categoryTextView.setText("CATEGORY: "+ monsterData.getCategory().toUpperCase());
        categoryTextView.setTextSize(18);
        categoryTextView.setTypeface(null, Typeface.BOLD);
        categoryTextView.setGravity(Gravity.CENTER);
        categoryTextView.setPadding(0,30,0,20);

        //Add elements to the view:
        final LinearLayout mainView = findViewById(R.id.MonsterStatsLayout);
        mainView.addView(monsterImage);
        mainView.addView(statsLayout);
        mainView.addView(combatRoleTextView);
        mainView.addView(categoryTextView);
    }
}
