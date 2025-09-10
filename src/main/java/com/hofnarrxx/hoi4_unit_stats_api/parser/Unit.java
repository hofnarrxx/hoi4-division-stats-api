package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Unit {
    private String name;
    private String id; //abbreviation
    private ArrayList<String> type;
    private ArrayList<String> categories;
    private String supportTypeBlock;
    private String group;
    private double combatWidth;
    private double hp;
    private double org;
    private double recoveryRate;
    private int manpower;
    private int trainingTime;
    private double suppression;
    private double weight;
    private double supplyConsumption;
    private double expLoss;
    private double reliability;
    private double recon;
    private double initiative;
    private double entrenchment;
    private double equipmentCapture;
    private Map<String, Integer> equipment;
    private Map<TerrainType, TerrainModifier> terrainModifiers;
    private Map<String, Double> supportNerfs;
    private ArrayList<BattalionMult> battalionMults; //only support companies have this 
 
    public Unit(String name) {
        this.name = name;
        this.type = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.equipment = new HashMap<>();
        this.terrainModifiers = new HashMap<>();
        for(TerrainType type : TerrainType.values()){
            terrainModifiers.put(type, new TerrainModifier(0, 0, 0));
        }
        this.supportNerfs = new HashMap<>();
        this.battalionMults = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public double getCombatWidth() {
        return combatWidth;
    }

    public void setCombatWidth(double combatWidth) {
        this.combatWidth = combatWidth;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getOrg() {
        return org;
    }

    public void setOrg(double org) {
        this.org = org;
    }

    public double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public int getManpower() {
        return manpower;
    }

    public void setManpower(int manpower) {
        this.manpower = manpower;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }

    public double getSuppression() {
        return suppression;
    }

    public void setSuppression(double suppression) {
        this.suppression = suppression;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getSupplyConsumption() {
        return supplyConsumption;
    }

    public void setSupplyConsumption(double supplyConsumption) {
        this.supplyConsumption = supplyConsumption;
    }

    public double getExpLoss() {
        return expLoss;
    }

    public void setExpLoss(double expLoss) {
        this.expLoss = expLoss;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }

    public double getRecon() {
        return recon;
    }

    public void setRecon(double recon) {
        this.recon = recon;
    }

    public double getInitiative() {
        return initiative;
    }

    public void setInitiative(double initiative) {
        this.initiative = initiative;
    }

    public double getEntrenchment() {
        return entrenchment;
    }

    public void setEntrenchment(double entrenchment) {
        this.entrenchment = entrenchment;
    }

    public double getEquipmentCapture() {
        return equipmentCapture;
    }

    public void setEquipmentCapture(double equipmentCapture) {
        this.equipmentCapture = equipmentCapture;
    }

    public String getSupportTypeBlock() {
        return supportTypeBlock;
    }

    public void setSupportTypeBlock(String supportTypeBlock) {
        this.supportTypeBlock = supportTypeBlock;
    }

    public void setEquipment(Map<String, Integer> equipment) {
        this.equipment = equipment;
    }

    public void setTerrainModifiers(Map<TerrainType, TerrainModifier> terrainModifiers) {
        this.terrainModifiers = terrainModifiers;
    }

    public Map<String, Integer> getEquipment(){
        return equipment;
    }

    public void addEquipment(String equipment, int amount) {
        this.equipment.put(equipment, amount);
    }

    public Map<TerrainType, TerrainModifier> getTerrainModifiers(){
        return terrainModifiers;
    }

    public void addTerrainModifier(TerrainType terrainType, TerrainModifier terrainModifier) {
        this.terrainModifiers.put(terrainType, terrainModifier);
    }

    @Override
    public String toString() {
        return name + ", " + id + ", " + hp + ", " + org + ", " + equipment + ", " + terrainModifiers;
    }

    public Map<String, Double> getSupportNerfs() {
        return supportNerfs;
    }

    public void setSupportNerfs(Map<String, Double> supportNerfs) {
        this.supportNerfs = supportNerfs;
    }

    public void addSupportNerf(String modifier, double value){
        this.supportNerfs.put(modifier, value);
    }

     public ArrayList<BattalionMult> getBattalionMults() {
        return battalionMults;
    }

    public void setBattalionMults(ArrayList<BattalionMult> battalionMults) {
        this.battalionMults = battalionMults;
    }

    public void addBattalionMult(BattalionMult bm){
        this.battalionMults.add(bm);
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category){
        categories.add(category);
    }
    
}
