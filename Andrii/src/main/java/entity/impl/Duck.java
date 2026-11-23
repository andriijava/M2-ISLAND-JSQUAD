package entity.impl;

import entity.abstraction.Animal;
import entity.abstraction.OrganismType;

public class Duck extends Animal{
    //
   public Duck () {

    super (OrganismType.DUCK);
   }
   @Override
   protected Animal createdChild() {
       return new Duck();
   }


}
