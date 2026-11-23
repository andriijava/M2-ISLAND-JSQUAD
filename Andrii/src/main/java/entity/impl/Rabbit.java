package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Rabbit extends Animal{
    public Rabbit(){
        super(OrganismType.RABBIT);
    }
    @Override
protected Animal createdChild(){
    return new Rabbit();
    }
}
