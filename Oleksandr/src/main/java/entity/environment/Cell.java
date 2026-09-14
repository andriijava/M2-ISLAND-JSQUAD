package entity.environment;

import entity.abstraction.Organism;
import java.util.ArrayList;
import java.util.List;
import entity.abstraction.Animal;
import entity.abstraction.OrganismType;
import entity.impl.Plant;

public class Cell {
   private final int x;
   private final int y;
   private final List<Organism> organisms = new ArrayList<>();

   public Cell(int x, int y) {
       this.x = x;
       this.y = y;
   }
   // Метод для відновлення та росту рослин на клітині
     public synchronized void growPlants(){
         // 1. Рахуємо поточну кількість рослин на даній клітині
         long plantCount = organisms.stream().filter(o -> o.getType() == OrganismType.PLANT).count();
         // 2. Якщо ліміт не досягнуто (менше 200), cпробуємо додати, наприклад, 5 нових рослин за один такт
         int maxPlants = OrganismType.PLANT.getMaxOnCell();
              for(int i = 0; i < 200; i++){
                  if(plantCount < maxPlants){
                      addOrganism(new Plant());
                      plantCount++;// збільшуємо лічильник, щоб не вийти за ліміт
                  }

         }
     }
     //Додавання організму під замком (монітором) клітини

   public synchronized void addOrganism(Organism organism){
       organisms.add(organism);
    }

   //Видалення організму під замком (помер чи пішов)

    public synchronized void removeOrganism(Organism organism) {
       organisms.remove(organism);
    }
//     Повертаємо знімок (копію) списку, щоб при обході одним потоком
//     інші потоки не провокували ConcurrentModificationException при зміні оригіналу
    public  synchronized List<Organism> getOrganisms() {
       return new ArrayList<>(organisms);   // в дужках (organisms) - це аргумент конструктора (джерело)
    }
    public int getX(){
       return x;
    }
    public int getY() {
       return y;
    }

    // Очищення клітини від усіх загиблих організмів
    public synchronized void removeDeadOrganisms() {
        // removeIf проходить по списку і видаляє кожен елемент, у якого getDead() повертає true
      organisms.removeIf(Organism:: getDead);
    }
    //Фаза живлення
    public synchronized void eat(){
      for(Organism organism : getOrganisms()){
          if(organism instanceof Animal animal && !animal.getDead()){
              animal.eat(this);
          }
      }
      removeDeadOrganisms();
    }
   //2.Фаза розмноження
   public synchronized void reproduce(){
       for (Organism organism : getOrganisms()){
           if (organism instanceof Animal animal && !animal.getDead())
               animal.reproduce(this);
       }
   }
// 3. Фаза пересування
    public synchronized void move(IslandMap map) {
       for (Organism organism : getOrganisms()){
           if(organism instanceof  Animal animal && !animal.getDead()){
               animal.move(this, map);
           }

       }
    }
    //4. Фаза зменшення ситості та очищення загиблих від голоду
     public synchronized void updateSatiety(){
       for (Organism organism : getOrganisms()){
           if (organism instanceof Animal animal && !animal.getDead()) {
             animal.decreaseSatiety();
           }
       }
       removeDeadOrganisms();

     }

}

