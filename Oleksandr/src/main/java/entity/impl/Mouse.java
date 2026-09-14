package entity.impl;

import entity.abstraction.OrganismType;
import entity.abstraction.Animal;


public class Mouse  extends Animal{
    public Mouse() {
        super(OrganismType.MOUSE);
    }
    @Override
    protected Animal createdChild(){
        return new Mouse();
    }

}
