
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Boa extends Animal {
    public Boa() {
        super(OrganismType.BOA);
    }

    @Override
    protected Animal createdChild() {
        return new Boa();
    }
}
