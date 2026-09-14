package entity.environment;

import entity.abstraction.OrganismType;
import java.util.EnumMap;
import java.util.Map;

public class EatingMatrix {
    private static final Map<OrganismType, Map<OrganismType, Integer>> MATRIX = new EnumMap<>(OrganismType.class);
    static {
  // 1.Створюємо базовий раціон для травоїдних: харчуються тільки рослинами зі 100% ймовірністю
        Map<OrganismType, Integer> herbivoreDiet = new EnumMap<>(OrganismType.class);
        herbivoreDiet.put(OrganismType.PLANT, 100);

        MATRIX.put(OrganismType.RABBIT, herbivoreDiet);
        MATRIX.put(OrganismType.HORSE, herbivoreDiet);
        MATRIX.put(OrganismType.DEER, herbivoreDiet);
        MATRIX.put(OrganismType.GOAT, herbivoreDiet);
        MATRIX.put(OrganismType.SHEEP, herbivoreDiet);
        MATRIX.put(OrganismType.BUFFALO, herbivoreDiet);
        MATRIX.put(OrganismType.CATERPILLAR, herbivoreDiet);

   // 2.Вовк
             Map<OrganismType, Integer> wolfDiet = new EnumMap<>(OrganismType.class);
        wolfDiet.put(OrganismType.RABBIT, 80);
        wolfDiet.put(OrganismType.HORSE, 10);
        wolfDiet.put(OrganismType.DEER, 15);
        wolfDiet.put(OrganismType.DUCK, 40);
        wolfDiet.put(OrganismType.GOAT, 60);
        wolfDiet.put(OrganismType.SHEEP, 70);
        wolfDiet.put(OrganismType.BOAR, 15);
        wolfDiet.put(OrganismType.BUFFALO, 10);
        wolfDiet.put(OrganismType.MOUSE, 80);
        MATRIX.put(OrganismType.WOLF, wolfDiet);
    //3. Лиса
              Map<OrganismType, Integer> foxDiet = new EnumMap<>(OrganismType.class);
          foxDiet.put(OrganismType.RABBIT, 70);
          foxDiet.put(OrganismType.DUCK, 60);
          foxDiet.put(OrganismType.CATERPILLAR, 40);
          foxDiet.put(OrganismType.MOUSE, 90);
          MATRIX.put(OrganismType.FOX, foxDiet);
    //4.Ведмiдь
           Map<OrganismType, Integer> bearDiet = new EnumMap<>(OrganismType.class);
        bearDiet.put(OrganismType.BOAR, 50);
        bearDiet.put(OrganismType.HORSE, 40);
        bearDiet.put(OrganismType.DEER, 80);
        bearDiet.put(OrganismType.RABBIT, 80);
        bearDiet.put(OrganismType.DUCK, 10);
        MATRIX.put(OrganismType.BEAR, bearDiet);
    // 5. Удав
                   Map<OrganismType, Integer> boaDiet = new EnumMap<>(OrganismType.class);
                boaDiet.put(OrganismType.FOX, 15);
                boaDiet.put(OrganismType.RABBIT, 20);
                boaDiet.put(OrganismType.MOUSE, 40);
                boaDiet.put(OrganismType.DUCK, 10);
                MATRIX.put(OrganismType.BOA, boaDiet);
    // 6.  Кабан
            Map<OrganismType, Integer> boarDiet = new EnumMap<>(OrganismType.class);
        boarDiet.put(OrganismType.MOUSE, 50);
        boarDiet.put(OrganismType.CATERPILLAR, 90);
        boarDiet.put(OrganismType.PLANT, 100);
        MATRIX.put(OrganismType.BOAR, boarDiet);

        //  Утка - усеядна
           Map<OrganismType, Integer> duckDiet = new EnumMap(OrganismType.class);
        duckDiet.put(OrganismType.CATERPILLAR, 90);
        duckDiet.put(OrganismType.PLANT, 100);
        MATRIX.put(OrganismType.DUCK, duckDiet);

        // 8. Миша  (усеядна)
           Map<OrganismType, Integer> mouseDiet = new EnumMap<>(OrganismType.class);
        mouseDiet.put(OrganismType.CATERPILLAR, 90);
        mouseDiet.put(OrganismType.PLANT, 100);
        MATRIX.put(OrganismType.MOUSE, mouseDiet);
    }
    public static int getProbability(OrganismType predator, OrganismType prey){
        // Отримуємо меню харчування для даного хижака/споживача
        Map<OrganismType, Integer> diet = MATRIX.get(predator);

        // Якщо раціону взагалі немає (наприклад, у рослин) — шанс 0%
           if(diet == null) {
               return 0;
           }
        // Шукаємо здобич у меню: якщо вона є — повертаємо ймовірність, якщо немає — 0% за замовчуванням
        return diet.getOrDefault(prey, 0);

    }
}


