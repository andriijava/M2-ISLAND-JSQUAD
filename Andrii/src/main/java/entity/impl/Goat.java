
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Goat extends Animal {
    public Goat() {
        super(OrganismType.GOAT);
    }

    @Override
    protected Animal createdChild() {
        return new Goat();
    }
}
