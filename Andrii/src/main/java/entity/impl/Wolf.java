package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Wolf extends Animal{
    public Wolf(){
        super(OrganismType.WOLF);
    }
    @Override
    protected Animal createdChild() {
        return new Wolf();

    }

}
