package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Fox extends Animal{
    public Fox(){
        super(OrganismType.FOX);

    }
    @Override
    protected Animal createdChild() {
        return new Fox();
    }
}
