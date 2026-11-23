package entity.abstraction;

public abstract class Organism {
    private final OrganismType type;
       private volatile boolean isDead = false;  //Прапор загибелі організму. Ключове слово volatile гарантує, що якщо один потік
// (наприклад, хижак) уб'є тварину, інші потоки миттєво побачать зміну статусу оперативної пам'яті.

    public  Organism(OrganismType type){
        this.type = type;
           }
    public OrganismType getType(){
        return type;
    }

    public double getWeight(){
        return type.getWeight();
    }
        public boolean getDead(){
        return isDead;
    }
    public void  setDead(boolean dead){
        isDead = dead;
    }


}



