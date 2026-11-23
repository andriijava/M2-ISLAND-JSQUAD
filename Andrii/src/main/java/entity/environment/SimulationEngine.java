package entity.environment;

import entity.abstraction.Organism;
import entity.abstraction.OrganismType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class SimulationEngine {

    private final IslandMap map;
    private final ScheduledExecutorService executor;

    public SimulationEngine(IslandMap map) {
        this.map = map;
        this.executor = Executors.newScheduledThreadPool(1);

    }
    //Створюємо пул потоків за кількістю ядер процесора, щоб програма працювала оптимально на будь-якому ПК.
    private final ExecutorService cellWorkerPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // Consumer<Cell> phaseAction — передаємо дію для клітини як параметр (наприклад, Cell::eat або Cell::reproduce)
     private void runPhaseParallel(Consumer<Cell> phaseAction){
         // Список для зберігання "квитанцій" (Future), які підтверджують вирушення задач
         List<Future<?>> futures = new ArrayList<>();
         // 1. Відправляємо кожну клітину острова на обробку в пул потоків
         for (int x = 0; x < map.getWidth(); x++){
             for(int y = 0; y < map.getHeight(); y++){
                 Cell cell = map.getCell(x, y);

                 // Передаємо задачу вільному потоку з cellWorkerPool
                 Future<?> future = cellWorkerPool.submit(() -> phaseAction.accept(cell));
                futures.add(future); //Зберігаємо квитанцію у список
             }
         }
         // 2. Точка синхронізації: чекаємо, поки ВСІ клітини завершать поточну фазу
         for(Future<?> future : futures){
             try{
                 future.get(); // Чекаємо завершення обробки конкретної клітини
             } catch (InterruptedException e){
                 // Повторний виклик Thread.currentThread().interrupt()
                 // відновлює прапорець переривання потоку в стан true
                 // (оскільки перехоплення InterruptedException автоматично скидає його в false).
                 // Це дає зовнішнім циклам та контролерам симуляції сигнал безпечно та коректно
                 // завершити свою роботу.
                 Thread.currentThread().interrupt();
             }catch(ExecutionException e){
                 // Помилка, якщо щось впало всередині самої клітини під час фази
               System.err.println("Помилка під час виконання фази: " + e.getCause());
             }
         }

     }
     private int currentTick = 0; //лічильник
     private static final int MAX_TICKS = 100; // ліміт тактів

    public void start() {
// Запускаємо метод step кожну секунду
        executor.scheduleAtFixedRate(this::step, 0, 1, TimeUnit.SECONDS);

    }

    private void step() {
        try {
            // Перевіряємо умову зупинки за лімітом тактів
            if (currentTick >= MAX_TICKS) {
                System.out.println("---Симуляція завершена: досягнуто ліміт тактів (" + MAX_TICKS + ") ---");
                stop();
                return;
            }
            currentTick++;
            System.out.println("=== Початок такту симуляції (" + currentTick + "/" + MAX_TICKS + ") ===");

            // 0. Фаза росту рослин (відновлення бази живлення)
            runPhaseParallel(Cell::growPlants);
            // 1. Фаза живлення (тварини їдять рослини або інших тварин)
            runPhaseParallel(Cell::eat);
            // 2. Фаза розмноження
            runPhaseParallel(Cell::reproduce);
            // 3. Фаза переміщення по карті
            runPhaseParallel(cell -> cell.move(map));
            // 4. Зменшення рівня ситості та зачистка тих, хто помер від голоду
            runPhaseParallel(Cell::updateSatiety);

            System.out.println("=== Такт завершено ===");

            // 2. Умова зупинки при вимиранні

            Map<OrganismType, Integer> stats = printStats();

            long totalAnimalsAlive = 0;
            for(Map.Entry<OrganismType, Integer> entry : stats.entrySet()){
                if(entry.getKey() != OrganismType.PLANT) {
                    totalAnimalsAlive += entry.getValue();
                }
            }
            if (totalAnimalsAlive <= 0) {
                System.out.println(("---Симуляція зупинена: на острові не залишилось жодної тварини (вимирання)---"));
                stop();
                return;
            }

        } catch (Exception e) {
            // Захист від "тихого падіння" шедулера
            System.err.println("Критична помилка в циклі симуляції: " + e.getMessage());
            e.printStackTrace();
            stop();// Зупиняємо шедулер у разі аварії
        }

    }

    private  Map<OrganismType, Integer> printStats() {
        Map<OrganismType, Integer> stats = new HashMap<>();

         for(int x = 0; x < map.getWidth(); x++){
             for(int y = 0; y < map.getHeight(); y++){
                 Cell cell = map.getCell(x, y);
                 for (Organism organism : cell.getOrganisms()){
                     if(!organism.getDead()){
                         //organism.getType() — отримує ключ (вид організму, наприклад WOLF або RABBIT).
                         //stats.getOrDefault(key, 0) — перевіряє, чи є вже цей вид у Map:
                        //Якщо вид вже є (наприклад, там лежить 5), метод поверне 5.
                         //Якщо виду ще немає в Map, метод поверне значення за замовчуванням 0 замість помилки чи null.
                         //+ 1 — додає одиницю до отриманого значення.
                         //stats.put(key, value) — зберігає підсумкове число назад у Map під відповідним ключем.
                         stats.put(organism.getType(), stats.getOrDefault(organism.getType(), 0) + 1);
                     }
                 }
             }
         }
      System.out.println("---Статистика острову---");
         for (Map.Entry<OrganismType, Integer>entry : stats.entrySet()){
             System.out.println(entry.getKey().getIcon()+ " " + " :   " + entry.getKey() + ": " + entry.getValue());
         }
        System.out.println("---------------------------\n");

         return stats;
    }
       public void stop () {
            executor.shutdown();
            cellWorkerPool.shutdown();
        }

    }
























