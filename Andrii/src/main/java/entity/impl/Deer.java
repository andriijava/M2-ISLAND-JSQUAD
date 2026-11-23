
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Deer extends Animal {
    public Deer() {
        super(OrganismType.DEER);
    }

    @Override
    protected Animal createdChild() {
        return new Deer();
    }
}
