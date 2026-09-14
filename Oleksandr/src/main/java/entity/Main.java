package entity;

import entity.environment.IslandMap;
import entity.environment.IslandInitializer;
import entity.environment.SimulationEngine;
import ua.com.javarush.jsquad.m1.island.Island;


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
