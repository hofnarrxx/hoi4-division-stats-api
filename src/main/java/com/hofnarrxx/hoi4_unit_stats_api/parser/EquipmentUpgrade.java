package com.hofnarrxx.hoi4_unit_stats_api.parser;

public class EquipmentUpgrade {
    String id;
    int maxLevel;
    double softAttack;
    double hardAttack;
    double apAttack;
    double airAttack;
    double armor;
    double maxSpeed;
    double reliability;
    double buildCostIc;

    EquipmentUpgrade(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
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

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }

    public double getBuildCostIc() {
        return buildCostIc;
    }

    public void setBuildCostIc(double buildCostIc) {
        this.buildCostIc = buildCostIc;
    }

}
