package entity.impl;

import entity.abstraction.Organism;
import entity.abstraction.OrganismType;

public class Plant extends Organism {
    public Plant(){
        super(OrganismType.PLANT);
    }
}
