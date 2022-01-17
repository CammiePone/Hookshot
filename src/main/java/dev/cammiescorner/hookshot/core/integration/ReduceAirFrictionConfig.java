package dev.cammiescorner.hookshot.core.integration;

// Taken from https://github.com/hatninja/Bhops
public class ReduceAirFrictionConfig {
    public static float sv_accelerate = 0.1F; //Ground acceleration. (originally .1)
    public static final double sv_gravity = 0.08D;
    public static float sv_airaccelerate = 0.2F; //Air acceleration. (originally .2)
    public static float sv_maxairspeed = 0.2F; //Maximum speed you can move in air without influence. Also determines how fast you gain bhop speed. (originally .08F)
    public static float maxSpeedMul = 2.2F; //How much to multiply default game's movementSpeed by.
}
