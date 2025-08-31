package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Equipment {
    private String id;
    private int year;
    private ArrayList<String> type;
    private boolean isArchetype;
    private String archetype;
    // misc
    private double reliability;
    private double maximumSpeed;
    private double buildCostIc;
    private double fuelConsumption;
    // defensive
    private double defense;
    private double breakthrough;
    private double hardness;
    private double armorValue;
    // offensive
    private double softAttack;
    private double hardAttack;
    private double apAttack;
    private double airAttack;

    public Equipment(String id) {
        this.id = id;
        this.type = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
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

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", year=" + year +
                ", defense=" + defense +
                ", softAttack=" + softAttack +
                '}';
    }

    public String getArchetype() {
        return archetype;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public boolean isArchetype() {
        return isArchetype;
    }

    public void setIsArchetype(boolean isArchetype) {
        this.isArchetype = isArchetype;
    }

    public void upgrage(Equipment modernEq){
        setYear(modernEq.getYear() != 0 ? modernEq.getYear() : year);
        setDefense(modernEq.getDefense() != 0 ? modernEq.getDefense() : defense);
        setBreakthrough(modernEq.getBreakthrough() != 0 ? modernEq.getBreakthrough() : breakthrough);
        setHardness(modernEq.getHardness() != 0 ? modernEq.getHardness() : hardness);
        setArmorValue(modernEq.getArmorValue() != 0 ? modernEq.getArmorValue() : armorValue);
        setSoftAttack(modernEq.getSoftAttack() != 0 ? modernEq.getSoftAttack() : softAttack);
        setHardAttack(modernEq.getHardAttack() != 0 ? modernEq.getHardAttack() : hardAttack);
        setAirAttack(modernEq.getAirAttack() != 0 ? modernEq.getAirAttack() : airAttack);
        setApAttack(modernEq.getApAttack() != 0 ? modernEq.getApAttack() : apAttack);
        setMaximumSpeed(modernEq.getMaximumSpeed() != 0 ? modernEq.getMaximumSpeed() : maximumSpeed);
        setBuildCostIc(modernEq.getBuildCostIc() != 0 ? modernEq.getBuildCostIc() : buildCostIc);
    }
}
