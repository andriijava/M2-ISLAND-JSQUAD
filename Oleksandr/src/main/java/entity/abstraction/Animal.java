package entity.abstraction;

import entity.environment.Cell;
import entity.environment.EatingMatrix;
import entity.environment.IslandMap;
import java.util.concurrent.ThreadLocalRandom;

public abstract  class Animal extends Organism{
    private double satiety;
    protected abstract Animal createdChild();

    public Animal(OrganismType type){
        super(type);
        this.satiety = type.getFoodRequired();
    }
//Зменшення ситості за один такт симуляції
    public void decreaseSatiety() {
  setSatiety(getSatiety() - getType().getFoodRequired() * 0.15);
    if(getSatiety() <= 0) {
        setDead(true);
    }
    }

    public double getSatiety(){
        return satiety;
    }
    public void setSatiety(double satiety){
        this.satiety = Math.max(0, Math.min(satiety,getType().getFoodRequired())); //Метод Math.max(0, ...) не дає значенню ситостi піти нижче 0
    }
    //Поведінка тварини за один хід :поедання здобичі

    public void eat(Cell currentCell){
        // Якщо тварина вже повністю сита, полювати не потрібно
        if (this.satiety >= getType().getFoodRequired()){
            return;
        }
       // Перебираємо всіх організмів у поточній клітині
          for(Organism target : currentCell.getOrganisms()){
              // Пропускаємо самих себе та вже мертвих істот
              if(target == this || target.getDead()){
                  continue;
              }
              // Отримуємо ймовірність з’їсти здобич з нашої EatingMatrix
              int probability = EatingMatrix.getProbability(this.getType(), target.getType());

              if (probability > 0) {
                  // Генеруємо випадкове число від 0 до 99
                  int randomValue = ThreadLocalRandom.current().nextInt(100);
                  if(randomValue < probability){
                      // Успішне полювання: позначаємо здобич мертвою та видаляємо з клітини,тобто if становиться  true
                  target.setDead(true);
                      // Поповнюємо ситість на вагу з'їденого (setSatiety сам обріже лишнє)
                      setSatiety(getSatiety() + target.getType().getWeight());
                      // УСПІХ: Пообідали — перериваємо пошук і виходимо з циклу!
                      break;

                  }
                  // Полювали один раз за хід (успішно чи ні) — виходимо з циклу
                 break;
              }
              // Якщо кубик видав невдачу — break НЕ спрацьовує,
              // і цикл іде перевіряти наступну жертву в цій же клітині

          }

    }
    public  void reproduce(Cell currentCell){
        // Поведінка тварини за один хід: розмноження
        int sameTypeCount = 0;

        // 1. Рахуємо всіх живих представників нашого виду в клітині (включаючи this)
        for(Organism organism : currentCell.getOrganisms()){
            if (organism.getType() == this.getType() && !organism.getDead()){
                sameTypeCount++;
            }

        }
        int maxCapacity = getType().getMaxOnCell();
        // 2. Якщо є хоча б 2 особини (партнер є) і не перевищено ліміт
        if(sameTypeCount >= 2 && sameTypeCount < maxCapacity) {
            Organism baby = createdChild();
            currentCell.addOrganism(baby);
        }

    }
    public  void move(Cell currentCell, IslandMap map){
           int maxSpeed = getType().getSpeed();
           if(maxSpeed == 0) {
               return; // Рослини та нерухомі організми залишаються на місці
           }
           int currentX = currentCell.getX();
           int currentY = currentCell.getY();
// Генеруємо випадкову кількість кроків від 0 до maxSpeed
        int steps = ThreadLocalRandom.current().nextInt();
         if(steps == 0){
             return;  // Тварина вирішила відпочити на цьому ході
         }
         int newX = currentX;
         int newY = currentY;

        // Вибираємо випадковий напрямок: 0 – вгору, 1 – вниз, 2 – вліво, 3 – вправо
        int direction = ThreadLocalRandom.current().nextInt();

        switch (direction){
            case 0 -> newX = Math.max(0, currentY - steps); // Вгору (обмеження зверху)
            case 1 -> newY = Math.min(map.getHeight() - 1, currentY + steps); // Вниз (обмеження знизу)
            case 2 -> newX = Math.max(0, currentX - steps);                     // Вліво (обмеження зліва)
            case 3 -> newX = Math.min(map.getWidth() - 1, currentX + steps);   // Вправо (обмеження справа)
        }
// Якщо координати змінилися - переїжджаємо до нової клітини
        if( newX != currentX || newY != currentY) {
            Cell targetCell = map.getCell(newX, newY);
            currentCell.removeOrganism(this);
            targetCell.addOrganism(this);
        }

    }


}


