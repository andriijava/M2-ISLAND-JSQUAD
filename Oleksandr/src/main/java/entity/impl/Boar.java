// Boar.java
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Boar extends Animal {
    public Boar() {
        super(OrganismType.BOAR);
    }

    @Override
    protected Animal createdChild() {
        return new Boar();
    }
}
