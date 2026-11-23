
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Horse extends Animal {
    public Horse() {
        super(OrganismType.HORSE);
    }

    @Override
    protected Animal createdChild() {
        return new Horse();
    }
}
