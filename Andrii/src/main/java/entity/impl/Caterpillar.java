// Caterpillar.java
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Caterpillar extends Animal {
    public Caterpillar() {
        super(OrganismType.CATERPILLAR);
    }

    @Override
    protected Animal createdChild() {
        return new Caterpillar();
    }
}
