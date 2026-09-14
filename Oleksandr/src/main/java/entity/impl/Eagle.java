
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Eagle extends Animal {
    public Eagle() {
        super(OrganismType.EAGLE);
    }

    @Override
    protected Animal createdChild() {
        return new Eagle();
    }
}
