// Buffalo.java
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Buffalo extends Animal {
    public Buffalo() {
        super(OrganismType.BUFFALO);
    }

    @Override
    protected Animal createdChild() {
        return new Buffalo();
    }
}
