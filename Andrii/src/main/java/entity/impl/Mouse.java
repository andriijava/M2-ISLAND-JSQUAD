package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;


public class Mouse  extends Animal{
    public Mouse() {
        super(OrganismType.MOUSE);
    }
    @Override
    protected Animal createdChild(){
        return new Mouse();
    }

}
