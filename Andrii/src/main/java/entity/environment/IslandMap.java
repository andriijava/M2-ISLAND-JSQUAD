package entity.environment;

public class IslandMap {
   private final int  width;
   private  final int height;
   private final Cell[][] cells;

   public IslandMap(int width, int height) {
       this.width = width;
       this.height = height;
       this.cells = new Cell[width][height];


       // Заповнюємо сітку новими клітинами
       for (int x = 0; x < width; x++) {
           for (int y = 0; y < height; y++) {
               cells[x][y] = new Cell(x, y);
           }
       }
   }

public Cell getCell(int x, int y) {
        return cells[x][y];
}
   public int getWidth() {
      return width;
    }
    public int getHeight() {
      return height;

    }

}

