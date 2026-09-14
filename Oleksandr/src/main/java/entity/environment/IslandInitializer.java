package entity.environment;

import entity.abstraction.Organism;
import entity.abstraction.OrganismType;
import entity.impl.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class IslandInitializer {

    private final IslandMap map;

    public IslandInitializer(IslandMap map) {
        this.map = map;
    }

    public void initialize() {
        // Проходимо координатами x і y з  IslandMap
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight();y++){
                Cell cell = map.getCell(x, y);
                  populateCell(cell);
            }
        }
 }
  private void populateCell(Cell cell) {
      // Хижаки
      addOrganisms(cell, OrganismType.WOLF, Wolf::new);
      addOrganisms(cell, OrganismType.FOX, Fox::new);
      addOrganisms(cell, OrganismType.BEAR, Bear::new);
      addOrganisms(cell, OrganismType.BOA, Boa::new);
      addOrganisms(cell, OrganismType.EAGLE, Eagle::new);

      // Травоїдні та всеїдні
      addOrganisms(cell, OrganismType.HORSE, Horse::new);
      addOrganisms(cell, OrganismType.DEER, Deer::new);
      addOrganisms(cell, OrganismType.RABBIT, Rabbit::new);
      addOrganisms(cell, OrganismType.MOUSE, Mouse::new);
      addOrganisms(cell, OrganismType.GOAT, Goat::new);
      addOrganisms(cell, OrganismType.SHEEP, Sheep::new);
      addOrganisms(cell, OrganismType.BOAR, Boar::new);
      addOrganisms(cell, OrganismType.BUFFALO, Buffalo::new);
      addOrganisms(cell, OrganismType.DUCK, Duck::new);
      addOrganisms(cell, OrganismType.CATERPILLAR, Caterpillar::new);

      // Флора
      addOrganisms(cell, OrganismType.PLANT, Plant::new);
  }
    //У методі addOrganisms ми оголосили третій параметр як Supplier<Organism> supplier.
    //Supplier (Постачальник) — це функціональний інтерфейс, який має лише один метод: .get().
    //Запис Wolf::new - це  скорочення лямбда-виразу () -> new Wolf() , наприклад
    //Якби ми передали просто new Wolf(), створився б один-єдиний вовк, і ми намагалися б додати один і той же об'єкт в клітинку кілька разів поспіль.
    //Передаючи Wolf::new, ми даємо циклу команду: "Кожного разу, коли викликається supplier.get(), створюй у пам'яті абсолютно нового вовка через new Wolf()".

    private void addOrganisms(Cell cell, OrganismType type, Supplier<Organism> supplier){
        int maxCount = type.getMaxOnCell();
         //Рандомно заселяємо.Рослини заселяємо побільше, а травоїдних урізаємо на старті в 15 разів:
            int divisor = (type == OrganismType.PLANT) ? 2 : 15;
        int count = ThreadLocalRandom.current().nextInt(0, maxCount / divisor + 1);

        for(int i = 0; i < count; i++){
               cell.addOrganism(supplier.get());//Виклик потокобезпечної реалізації

        }

    }
}
