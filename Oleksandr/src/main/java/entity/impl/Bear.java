
package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Bear extends Animal {
    public Bear() {
        super(OrganismType.BEAR);
    }

    @Override
    protected Animal createdChild() {
        return new Bear();
    }
}