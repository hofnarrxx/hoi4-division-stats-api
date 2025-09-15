package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.HashMap;
import java.util.Map;

import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainModifier;
import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainType;

public class DivStatsDTO {
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
    private double reliabilityBonus;
    private double recon;
    private double initiative;
    private double entrenchment;
    private double equipmentCapture;
    private double casualtyTrickleback;
    private Map<String, Integer> equipment;
    private Map<TerrainType, TerrainModifier> terrainModifiers;

    // private double reliability;
    private double maximumSpeed;
    private double buildCostIc;
    private double fuelConsumption;
    private double defense;
    private double breakthrough;
    private double hardness;
    private double armorValue;
    private double softAttack;
    private double hardAttack;
    private double apAttack;
    private double airAttack;

    public DivStatsDTO() {
        this.equipment = new HashMap<>();
        this.terrainModifiers = new HashMap<>();
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

    public Map<String, Integer> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<String, Integer> equipment) {
        this.equipment = equipment;
    }

    public void addEquipment(String name, int amount){
        this.equipment.put(name, amount);
    }

    public Map<TerrainType, TerrainModifier> getTerrainModifiers() {
        return terrainModifiers;
    }

    public void setTerrainModifiers(Map<TerrainType, TerrainModifier> terrainModifiers) {
        this.terrainModifiers = terrainModifiers;
    }

    public double getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(double maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public double getBuildCostIc() {
        return buildCostIc;
    }

    public void setBuildCostIc(double buildCostIc) {
        this.buildCostIc = buildCostIc;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getBreakthrough() {
        return breakthrough;
    }

    public void setBreakthrough(double breakthrough) {
        this.breakthrough = breakthrough;
    }

    public double getHardness() {
        return hardness;
    }

    public void setHardness(double hardness) {
        this.hardness = hardness;
    }

    public double getArmorValue() {
        return armorValue;
    }

    public void setArmorValue(double armorValue) {
        this.armorValue = armorValue;
    }

    public double getSoftAttack() {
        return softAttack;
    }

    public void setSoftAttack(double softAttack) {
        this.softAttack = softAttack;
    }

    public double getHardAttack() {
        return hardAttack;
    }

    public void setHardAttack(double hardAttack) {
        this.hardAttack = hardAttack;
    }

    public double getApAttack() {
        return apAttack;
    }

    public void setApAttack(double apAttack) {
        this.apAttack = apAttack;
    }

    public double getAirAttack() {
        return airAttack;
    }

    public void setAirAttack(double airAttack) {
        this.airAttack = airAttack;
    }

    public double getReliabilityBonus() {
        return reliabilityBonus;
    }

    public void setReliabilityBonus(double reliabilityBonus) {
        this.reliabilityBonus = reliabilityBonus;
    }

    public double getCasualtyTrickleback() {
        return casualtyTrickleback;
    }

    public void setCasualtyTrickleback(double casualtyTrickleback) {
        this.casualtyTrickleback = casualtyTrickleback;
    }

    
}
