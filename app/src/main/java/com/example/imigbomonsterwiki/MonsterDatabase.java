package com.example.imigbomonsterwiki;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MonsterDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MonsterLegends";
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

    private SharedPreferences contMonsters;

    public  MonsterDatabase(Context context, boolean reset) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        contMonsters = context.getSharedPreferences("contMonsters", Context.MODE_PRIVATE);

        if(reset){
            context.deleteDatabase(DATABASE_NAME);
            SharedPreferences.Editor editor = contMonsters.edit();
            editor.putInt("storedMonstersAmount",0);
            editor.commit();
        }
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

    public void addMonster(final MonsterData monsterData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues monsterValues = new ContentValues();

        monsterValues.put("_ID", monsterData.getId());
        monsterValues.put("NAME", monsterData.getName());

        ArrayList<String> elements = monsterData.getAttributes();
        monsterValues.put("ELEMENT1", elements.get(0));

        if(elements.size()==2){
            monsterValues.put("ELEMENT2", monsterData.getAttributes().get(1));
        }

        monsterValues.put("RARITY", monsterData.getRarity());
        monsterValues.put("IMAGE_NAME", monsterData.getImageKey());

        monsterValues.put("LIFE", monsterData.getLife());
        monsterValues.put("POWER", monsterData.getStrength());
        monsterValues.put("SPEED", monsterData.getSpeed());
        monsterValues.put("STAMINA", monsterData.getStamina());

        monsterValues.put("CATEGORY", monsterData.getCategory());
        monsterValues.put("ROLE", monsterData.getCombatRole());

        db.insert("MONSTERS", null, monsterValues);
        SharedPreferences.Editor editor = contMonsters.edit();
        editor.putInt("storedMonstersAmount",contMonsters.getInt("storedMonstersAmount",0)+1);
        editor.commit();
    }

    public ArrayList<MonsterDisplayData> getMonstersDisplayOrderByRelease(int begin, int end){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _ID,IMAGE_NAME FROM MONSTERS ORDER BY _ID DESC LIMIT "+begin+", "+end, null);

        ArrayList<MonsterDisplayData> monsters = new ArrayList();
        while(cursor.moveToNext()){
            MonsterDisplayData m = new MonsterDisplayData();
            m.setId(cursor.getInt(0));
            m.setImageKey(cursor.getString(1));

            monsters.add(m);
        }

        cursor.close();
        db.close();
        return monsters;
    }

    public ArrayList<MonsterDisplayData> getMonstersDisplayOrderByRelease(int limit, int offset, String whereClause){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _ID,IMAGE_NAME FROM MONSTERS "+whereClause+" ORDER BY _ID DESC LIMIT "+limit+" OFFSET "+offset, null);

        ArrayList<MonsterDisplayData> monsters = new ArrayList();
        while(cursor.moveToNext()){
            MonsterDisplayData m = new MonsterDisplayData();
            m.setId(cursor.getInt(0));
            m.setImageKey(cursor.getString(1));

            monsters.add(m);
        }

        db.close();
        cursor.close();
        return monsters;
    }

    public MonsterData getMonsterByID(int id){
        final SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MONSTERS WHERE _ID = "+id, null);
        cursor.moveToNext(); //This will always return 1 value.

        //Set MonsterData Data:
        MonsterData monsterData = new MonsterData(cursor.getInt(0), cursor.getString(1));
        monsterData.addAttribute(cursor.getString(2));
        monsterData.addAttribute(cursor.getString(3));
        monsterData.setRarity(cursor.getInt(4));
        monsterData.setImageKey(cursor.getString(5));
        monsterData.setLife(cursor.getInt(6));
        monsterData.setStrength(cursor.getInt(7));
        monsterData.setSpeed(cursor.getInt(8));
        monsterData.setStamina(cursor.getInt(9));
        monsterData.setCategory(cursor.getString(10));
        monsterData.setCombatRole(cursor.getString(11));

        db.close();
        cursor.close();
        return monsterData;
    }

    public int getMonstersAmount(String whereClause){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if(whereClause.isEmpty()){
            cursor = db.rawQuery("SELECT COUNT(_ID) FROM MONSTERS", null);
        }else{
            cursor = db.rawQuery("SELECT COUNT(_ID) FROM MONSTERS "+whereClause, null);
        }

        cursor.moveToNext();
        final int amount = cursor.getInt(0);

        db.close();
        cursor.close();
        return amount;
    }

    public void parseMonstersJSON(){
        MonsterJSONParser jsonParser = new MonsterJSONParser();
        jsonParser.execute(contMonsters.getInt("storedMonstersAmount",0), this);
    }
}
