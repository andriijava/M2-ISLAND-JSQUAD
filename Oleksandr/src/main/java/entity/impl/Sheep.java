
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Sheep extends Animal {
    public Sheep() {
        super(OrganismType.SHEEP);
    }

    @Override
    protected Animal createdChild() {
        return new Sheep();
    }
}
