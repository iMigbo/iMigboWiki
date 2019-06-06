package com.example.imigbomonsterwiki;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MonsterDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MonsterLegends";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_MONSTERS =
            "CREATE TABLE MONSTERS (" +
                    "_ID INTEGER PRIMARY KEY, " +
                    "NAME TEXT, " +
                    "ELEMENT1 TEXT, " +
                    "ELEMENT2 TEXT, " +
                    "RARITY INTEGER, " +
                    "IMAGE_NAME TEXT, " +
                    "LIFE INTEGER, " +
                    "POWER INTEGER, " +
                    "SPEED INTEGER, " +
                    "STAMINA INTEGER,"+
                    "CATEGORY TEXT,"+
                    "ROLE TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS MONSTER";

    public  MonsterDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SharedPreferences contMonsters = context.getSharedPreferences("contMonsters", Context.MODE_PRIVATE);
        JSONParser jsonParser = new JSONParser();
        jsonParser.execute(contMonsters.getInt("storedMonstersAmount",0), this);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MONSTERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void addMonster(final Monster monster){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues monsterValues = new ContentValues();

        monsterValues.put("_ID", monster.getId());
        monsterValues.put("NAME", monster.getName());

        ArrayList<String> elements = monster.getAttributes();
        monsterValues.put("ELEMENT1", elements.get(0));

        if(elements.size()==2){
            monsterValues.put("ELEMENT2", monster.getAttributes().get(1));
        }

        monsterValues.put("RARITY", monster.getRarity());
        monsterValues.put("IMAGE_NAME", monster.getImageName());

        monsterValues.put("LIFE", monster.getLife());
        monsterValues.put("POWER", monster.getStrength());
        monsterValues.put("SPEED", monster.getSpeed());
        monsterValues.put("STAMINA", monster.getStamina());

        monsterValues.put("CATEGORY", monster.getCategory());
        monsterValues.put("ROLE", monster.getCombatRole());

        db.insert("MONSTERS", null, monsterValues);
    }

    public ArrayList<Monster> getMonstersDisplayOrderByID(){
        final String[] columns = {"_ID","NAME","IMAGE_NAME","RARITY","TYPE"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _ID,NAME,IMAGE_NAME,RARITY,CATEGORY FROM MONSTERS", null);

        ArrayList<Monster> monsters = new ArrayList();
        while(cursor.moveToNext()){
            Monster m = new Monster(cursor.getInt(0), cursor.getString(1));
            m.setImageName(cursor.getString(2));
            m.setRarity(cursor.getInt(3));
            m.setCategory(cursor.getString(4));

            monsters.add(m);
        }
        cursor.close();
        return monsters;
    }
}
