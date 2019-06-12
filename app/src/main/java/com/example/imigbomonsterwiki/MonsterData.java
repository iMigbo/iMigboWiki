package com.example.imigbomonsterwiki;

import java.io.Serializable;
import java.util.ArrayList;

public class MonsterData implements Serializable {
    private int id;
    private String name;
    private ArrayList<String> attributes = new ArrayList<>();
    private int rarity;
    private String imageKey;
    private ArrayList<Integer> attacks = new ArrayList<>(); //Pending JSON
    private int specialAttack; //Pending JSON
    private ArrayList<Integer> traits = new ArrayList<>(); //Pending JSON
    private int life;
    private int strength;
    private int speed;
    private int stamina;
    private String category;
    private ArrayList <Integer> relics = new ArrayList<>(); //Pending JSON
    private String combatRole;
    private ArrayList <Object> books = new ArrayList<>(); //Pending JSON

    public MonsterData(int id, String name)
    {
        this.id = id;
        this.setName(name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getCombatRole() {
        return combatRole;
    }

    public void setCombatRole(String combatRole) {
        this.combatRole = combatRole;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public ArrayList<Object> getBooks() {
        return books;
    }

    public void addBook(Object book) {
        if(!books.contains(book)){
            this.books.add(book);
        }
    }

    public ArrayList<Integer> getAttacks() {
        return attacks;
    }

    public void addAttack(int attack) {
        attacks.add(attack);
    }

    public ArrayList<Integer> getRelics() {
        return relics;
    }

    public void addRelic(int relic) {
        if(relics.size() < 2){
            relics.add(relic);
        }
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void addAttribute(String attribute) {
        if(attributes.size()<2)
        {
            attributes.add(attribute);
        }
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public ArrayList<Integer> getTraits() {
        return traits;
    }

    public void addTrait(int trait) {
        this.traits.add(trait);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
