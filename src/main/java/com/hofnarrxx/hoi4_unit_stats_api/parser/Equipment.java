package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Equipment {
    private String id;
    private int year;
    private ArrayList<String> type;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }

    public void setMaximumSpeed(double maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public void setBreakthrough(double breakthrough) {
        this.breakthrough = breakthrough;
    }

    public void setHardness(double hardness) {
        this.hardness = hardness;
    }

    public void setArmorValue(double armorValue) {
        this.armorValue = armorValue;
    }

    public void setSoftAttack(double softAttack) {
        this.softAttack = softAttack;
    }

    public void setHardAttack(double hardAttack) {
        this.hardAttack = hardAttack;
    }

    public void setApAttack(double apAttack) {
        this.apAttack = apAttack;
    }

    public void setAirAttack(double airAttack) {
        this.airAttack = airAttack;
    }

    public void setBuildCostIc(double buildCostIc) {
        this.buildCostIc = buildCostIc;
    }

    public void addType(String type) {
        this.type.add(type);
    }

    public void setType(ArrayList<String> types) {
        this.type = types;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    @Override
    public String toString() {
        return "InfantryEquipment{" +
                "id='" + id + '\'' +
                ", year=" + year +
                ", defense=" + defense +
                ", softAttack=" + softAttack +
                '}';
    }
}
