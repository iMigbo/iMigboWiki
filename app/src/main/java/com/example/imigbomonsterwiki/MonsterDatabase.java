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
        monsterValues.put("IMAGE_NAME", monster.getImageKey());

        monsterValues.put("LIFE", monster.getLife());
        monsterValues.put("POWER", monster.getStrength());
        monsterValues.put("SPEED", monster.getSpeed());
        monsterValues.put("STAMINA", monster.getStamina());

        monsterValues.put("CATEGORY", monster.getCategory());
        monsterValues.put("ROLE", monster.getCombatRole());

        db.insert("MONSTERS", null, monsterValues);
        SharedPreferences.Editor editor = contMonsters.edit();
        editor.putInt("storedMonstersAmount",contMonsters.getInt("storedMonstersAmount",0)+1);
        editor.commit();
    }

    public ArrayList<MonsterDisplay> getMonstersDisplayOrderByRelease(int begin, int end){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _ID,IMAGE_NAME FROM MONSTERS ORDER BY _ID DESC LIMIT "+begin+", "+end, null);

        ArrayList<MonsterDisplay> monsters = new ArrayList();
        while(cursor.moveToNext()){
            MonsterDisplay m = new MonsterDisplay();
            m.setId(cursor.getInt(0));
            m.setImageKey(cursor.getString(1));

            monsters.add(m);
        }

        cursor.close();
        db.close();
        return monsters;
    }

    public ArrayList<MonsterDisplay> getMonstersDisplayOrderByRelease(int limit, int offset, String whereClause){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _ID,IMAGE_NAME FROM MONSTERS "+whereClause+" ORDER BY _ID DESC LIMIT "+limit+" OFFSET "+offset, null);

        ArrayList<MonsterDisplay> monsters = new ArrayList();
        while(cursor.moveToNext()){
            MonsterDisplay m = new MonsterDisplay();
            m.setId(cursor.getInt(0));
            m.setImageKey(cursor.getString(1));

            monsters.add(m);
        }

        db.close();
        cursor.close();
        return monsters;
    }

    public Monster getMonsterByID(int id){
        final SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MONSTERS WHERE _ID = "+id, null);
        cursor.moveToNext(); //This will always return 1 value.

        //Set Monster Data:
        Monster monster = new Monster(cursor.getInt(0), cursor.getString(1));
        monster.addAttribute(cursor.getString(2));
        monster.addAttribute(cursor.getString(3));
        monster.setRarity(cursor.getInt(4));
        monster.setImageKey(cursor.getString(5));
        monster.setLife(cursor.getInt(6));
        monster.setStrength(cursor.getInt(7));
        monster.setSpeed(cursor.getInt(8));
        monster.setStamina(cursor.getInt(9));
        monster.setCategory(cursor.getString(10));
        monster.setCombatRole(cursor.getString(11));

        db.close();
        cursor.close();
        return monster;
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
        JSONParser jsonParser = new JSONParser();
        jsonParser.execute(contMonsters.getInt("storedMonstersAmount",0), this);
    }
}
