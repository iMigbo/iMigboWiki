package com.example.imigbomonsterwiki;

import android.os.AsyncTask;

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

public class JSONParser extends AsyncTask<Object, Monster[], Object> {

    private static final String monsterJSONURL = "https://mcx.socialpointgames.com/bs_toolbox/get_config?collection=items.units&columns=id,name,nameKey,attributes,rarity,img_name,tier_1,tier_2,tier_3,tier_4,special_skill,traits,life,power,speed,stamina,monster_origin,relics_categories,monster_combat_role";

    private MonsterDatabase database;

    public JSONParser()
    {

    }

    @Override
    protected Object doInBackground(Object... params){
        final int storedMonsters = (int) params[0];
        database = (MonsterDatabase) params[1];

        try{
            final URL itemsURL = new URL(monsterJSONURL);
            final HttpURLConnection conn = (HttpURLConnection) itemsURL.openConnection();

            final InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = bufferedReader.readLine();
            JSONArray monsterArray = new JSONArray(line);
            for (int i = 0; i < monsterArray.length() - storedMonsters; i++) {
                JSONObject monsterData = (JSONObject) monsterArray.get(i);
                final int id =  monsterData.getInt("id");
                final String name = monsterData.getString("name");

                Monster monster = new Monster(id, name);

                final JSONArray attributes = monsterData.getJSONArray("attributes");
                for(byte j = 0; j < attributes.length(); j++){
                    monster.addAttribute(attributes.getString(j));
                }

                monster.setRarity(monsterData.getInt("rarity"));
                monster.setImageKey(monsterData.getString("img_name"));

                //The attacks can be 9 or 12 depending of the monster
                addAttacks(monsterData.getJSONArray("tier_1"), monster);
                addAttacks(monsterData.getJSONArray("tier_2"), monster);
                final JSONArray attacksTier3 = monsterData.getJSONArray("tier_3");
                addAttacks(attacksTier3, monster);
                final JSONArray attacksTier4 = monsterData.getJSONArray("tier_4");
                if(attacksTier3.getInt(0) != attacksTier4.getInt(0)){
                    addAttacks(attacksTier4, monster);
                }

                monster.setSpecialAttack(monsterData.getInt("special_skill"));

                final JSONArray traits = monsterData.getJSONArray("traits");
                for(byte j = 0; j < traits.length(); j++){
                    monster.addTrait(traits.getInt(j));
                }

                monster.setLife(monsterData.getInt("life"));
                monster.setStrength(monsterData.getInt("power"));
                monster.setSpeed(monsterData.getInt("speed"));
                monster.setStamina(monsterData.getInt("stamina"));
                monster.setCategory(monsterData.getString("monster_origin"));

                final JSONArray relics = monsterData.getJSONArray("relics_categories");
                for(byte j = 0; j < relics.length(); j++){
                    monster.addRelic(relics.getInt(j));
                }

                monster.setCombatRole(monsterData.getString("monster_combat_role"));

                database.addMonster(monster);
            }
            bufferedReader.close();
        }catch (MalformedURLException e) {
            System.err.println(e.toString());
        }catch (IOException e){
            System.err.println(e.toString());
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o)
    {

    }

    private void addAttacks(final JSONArray tierAttacks, Monster monster) throws JSONException {
        for(byte j = 0; j < tierAttacks.length(); j++){
            monster.addAttack(tierAttacks.getInt(j));
        }
    }
}
