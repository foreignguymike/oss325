package com.distraction.oss325.entity;

import com.distraction.oss325.Context;

import java.util.List;

public abstract class Interactable extends Entity {

    public abstract void interact(Context context, Player player, List<Particle> particles);

}
