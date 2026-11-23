package entity;

import entity.environment.IslandInitializer;
import entity.environment.IslandMap;
import entity.environment.SimulationEngine;


public class Main {
    public static void main (String [] args){
    // 1.Створюємо карту (100х20 )
        IslandMap map = new IslandMap(100,20);
        //2.Заселяємо її
        IslandInitializer initializer = new IslandInitializer(map);
        initializer.initialize();

        //3. Запускаємо двигун симуляції
        SimulationEngine engine = new SimulationEngine(map);
        engine.start();

    }

}
