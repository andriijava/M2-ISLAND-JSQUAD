package entity.abstraction;

import entity.environment.Cell;
import entity.environment.EatingMatrix;
import entity.environment.IslandMap;
import java.util.concurrent.ThreadLocalRandom;

public abstract  class Animal extends Organism {
    private double satiety;

    protected abstract Animal createdChild();

    public Animal(OrganismType type) {
        super(type);
        this.satiety = type.getFoodRequired();
    }

    //Зменшення ситості за один такт симуляції
    public void decreaseSatiety() {
        if (getType().getFoodRequired() == 0) {
            return;
        }
        double hungerRate;
        if (getType() == OrganismType.HORSE
                || getType() == OrganismType.DEER
                || getType() == OrganismType.BUFFALO) {
            hungerRate = 0.05;
        } else if (getType() == OrganismType.GOAT
                || getType() == OrganismType.SHEEP
                || getType() == OrganismType.BOAR) {
            hungerRate = 0.06;
        } else if (getType() == OrganismType.MOUSE
                || getType() == OrganismType.RABBIT
                || getType() == OrganismType.DUCK) {
            hungerRate = 0.07;
        } else {
            hungerRate = 0.09;
        }

        setSatiety(getSatiety() - getType().getFoodRequired() * hungerRate);
        if (getSatiety() <= 0) {
            setDead(true);
        }
    }

    public double getSatiety() {
        return satiety;
    }

    public void setSatiety(double satiety) {
        this.satiety = Math.max(0, Math.min(satiety, getType().getFoodRequired())); //Метод Math.max(0, ...) не дозволяє значенню ситості опуститися нижче 0.
    }
    //Поведінка тварини за один хід :поедання здобичі

    public void eat(Cell currentCell) {
        // Якщо тварина вже повністю сита, полювати не потрібно
        if (this.satiety >= getType().getFoodRequired()) {
            return;
        }
        int attempts = 0;
        int maxAttempts = 3;
        int mealsEaten = 0;
        int maxMeals = 1;

        // Перебираємо всіх організмів у поточній клітині
        for (Organism target : currentCell.getOrganisms()) {
            // Пропускаємо самих себе та вже мертвих істот
            if (target == this || target.getDead()) {
                continue;
            }
            // Отримуємо ймовірність з’їсти здобич з нашої EatingMatrix
            int probability = EatingMatrix.getProbability(this.getType(), target.getType());

            if (probability > 0) {
                attempts++;
                // Генеруємо випадкове число від 0 до 99
                int randomValue = ThreadLocalRandom.current().nextInt(100);
                if (randomValue < probability) {

                    // Успішне полювання: позначаємо здобич мертвою
                    target.setDead(true);

                    // Обмежуємо приріст ситості за один укус, щоб не наїдатися "впрок" за один раз
                    double maxGainPerMeal = getType().getFoodRequired() * 0.50;
                    double gain = Math.min(target.getType().getWeight(), maxGainPerMeal);
                    setSatiety(getSatiety() + gain);

                    mealsEaten++;
                    // Не більше одного успішного поїдання за такт або якщо вже наїлися до максимуму
                    if (mealsEaten >= maxMeals || getSatiety() >= getType().getFoodRequired()) {
                        break;
                    }
                }
                // Не робимо нескінченну кількість спроб за один такт
                if (attempts >= maxAttempts) {
                    break;
                }
            }
        }
    }
    public void reproduce(Cell currentCell) {
        // Поведінка тварини за один хід: розмноження
        int sameTypeCount = 0;

        // 1. Рахуємо всіх живих представників нашого виду в клітині (включаючи this)
        for (Organism organism : currentCell.getOrganisms()) {
            if (organism.getType() == this.getType() && !organism.getDead()) {
                sameTypeCount++;
            }
        }

        int maxCapacity = getType().getMaxOnCell();
        // 2. Перевіряємо, чи харчується тварина рослинами (травоїдна)
        boolean isHerbivore = EatingMatrix.getProbability(this.getType(), OrganismType.PLANT) > 0;
        // 3. Базова перевірка: є хоча б 2 особини (партнер є) і не перевищено ліміт клітини
        boolean canReproduce = (sameTypeCount >= 2) && (sameTypeCount < maxCapacity);

        if (canReproduce) {
            double minSatiety;
            int chance;

            // 4. Для травоїдних
            if (isHerbivore) {
                boolean isSmallPrey = (getType() == OrganismType.CATERPILLAR
                        || getType() == OrganismType.MOUSE
                        || getType() == OrganismType.RABBIT
                        || getType() == OrganismType.DUCK);

                boolean isLargeHerbivore = (getType() == OrganismType.HORSE
                        || getType() == OrganismType.DEER
                        || getType() == OrganismType.BUFFALO);

                minSatiety = getType().getFoodRequired() * (isLargeHerbivore ? 0.42: (isSmallPrey ? 0.60 : 0.45));

                if (getType() == OrganismType.MOUSE) {
                    chance = 13;
                    minSatiety = getType().getFoodRequired() * 0.26 ;
                } else if (getType() == OrganismType.CATERPILLAR) {
                    chance = 25;
                    minSatiety = getType().getFoodRequired() * 0.14;
                } else if (getType() == OrganismType.DUCK ) {
                    chance = 5;
                    minSatiety = getType().getFoodRequired() * 0.37;
                } else if (getType() == OrganismType.RABBIT) {
                    chance = 6;
                    minSatiety = getType().getFoodRequired() * 0.18;
                }  else if (getType() == OrganismType.BUFFALO) {
                    chance = 13;
                    minSatiety = getType().getFoodRequired() * 0.38;
                }
                else if (isLargeHerbivore) {
                    chance = 10;
                } else if (getType() == OrganismType.BOAR) {
                    chance = 8;
                    minSatiety = getType().getFoodRequired() * 0.40;
                }
                else if (getType() == OrganismType.GOAT || getType() == OrganismType.SHEEP) {
                    chance = 2;
                } else {
                    chance = 6;
                }
            } else {
                // 4a. Для хижаків
                boolean isApex = (getType() == OrganismType.WOLF || getType() == OrganismType.BEAR);
                minSatiety = getType().getFoodRequired() * (isApex ? 1: 0.95);

                if (isApex) {
                    chance = 1;
                } else if (getType() == OrganismType.BOA) {
                    chance = 17;
                    minSatiety = getType().getFoodRequired() * 0.53;
                } else if (getType() == OrganismType.FOX) {
                    chance = 7;
                    minSatiety = getType().getFoodRequired() * 0.38;
                }else if (getType() == OrganismType.EAGLE) {
                    chance = 3;
                    minSatiety = getType().getFoodRequired() * 0.68;
                }

                else {
                    chance = 1;
                }
            }

            canReproduce = (getSatiety() >= minSatiety)
                    && (ThreadLocalRandom.current().nextInt(100) < chance);
        }

        // 5. Якщо всі умови виконано — створюємо потомство та додаємо його до клітини
        if (canReproduce) {
            Organism baby = createdChild();
            currentCell.addOrganism(baby);
        }
    }

    public void move(Cell currentCell, IslandMap map) {
        int maxSpeed = getType().getSpeed();
        if (maxSpeed == 0) {
            return; // Рослини та нерухомі організми залишаються на місці
        }
        int currentX = currentCell.getX();
        int currentY = currentCell.getY();
// Генеруємо випадкову кількість кроків від 0 до maxSpeed
        int steps = ThreadLocalRandom.current().nextInt(maxSpeed + 1);
        if (steps == 0) {
            return;  // Тварина вирішила відпочити на цьому ході
        }
        int newX = currentX;
        int newY = currentY;

        // Вибираємо випадковий напрямок: 0 – вгору, 1 – вниз, 2 – вліво, 3 – вправо
        int direction = ThreadLocalRandom.current().nextInt(4);

        switch (direction) {
            case 0 -> newY = Math.max(0, currentY - steps); // Вгору (обмеження зверху)
            case 1 -> newY = Math.min(map.getHeight() - 1, currentY + steps); // Вниз (обмеження знизу)
            case 2 -> newX = Math.max(0, currentX - steps);                     // Вліво (обмеження зліва)
            case 3 -> newX = Math.min(map.getWidth() - 1, currentX + steps);   // Вправо (обмеження справа)
        }
// Якщо координати змінилися - переїжджаємо до нової клітини

        if (newX != currentX || newY != currentY) {
            Cell targetCell = map.getCell(newX, newY);

            Cell firstLock = (currentCell.getX() < targetCell.getX() || (currentCell.getX() == targetCell.getX() &&
                    currentCell.getY() < targetCell.getY())) ? currentCell : targetCell;
            Cell secondLock = (firstLock == currentCell) ? targetCell : currentCell;
            synchronized (firstLock) {
                synchronized (secondLock) {
                    currentCell.removeOrganism(this);
                    targetCell.addOrganism(this);
                }
            }

        }


    }

}

