package com.hofnarrxx.hoi4_unit_stats_api.parser;

public class TerrainModifier {
    private TerrainType terrain;
    private double attackModifier;
    private double defenseModifier;
    private double movementModifier;

    public TerrainModifier(TerrainType terrain, double attack, double defense, double movement) {
        this.terrain = terrain;
        this.attackModifier = attack;
        this.defenseModifier = defense;
        this.movementModifier = movement;
    }

    public double getAttackModifier() {
        return attackModifier;
    }

    public double getDefenseModifier() {
        return defenseModifier;
    }

    public double getMovementModifier() {
        return movementModifier;
    }

    public TerrainType getTerrain(){
        return terrain;
    }

    @Override
    public String toString() {
        return "{attack = " + attackModifier + ", " + "defense = " + defenseModifier + ", movement = "
                + movementModifier + "}";
    }

    public TerrainModifier add(TerrainModifier other){
        return new TerrainModifier(terrain,attackModifier + other.attackModifier, defenseModifier + other.defenseModifier,
                movementModifier + other.movementModifier);
    }

    public TerrainModifier divide(double divider){
        return new TerrainModifier(terrain,attackModifier/divider, defenseModifier/divider, movementModifier/divider);
    }
}
