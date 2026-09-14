package entity.abstraction;

public enum OrganismType {
    // ХИЖАКИ
    // Послiдовнicть параметрiв :(weight, maxOnCell, speed,foodRequired)
    WOLF("🐺", 50, 30, 3, 8),
    BOA("🐍", 15, 30, 1, 3),
    FOX("🦊", 8, 30, 2, 2),
    BEAR("🐻", 500, 5, 2, 80),
    EAGLE("🦅", 6, 20, 3, 1),

    // РОСЛИННОЯДНI
    HORSE("🐎", 400, 20, 4, 60),
    DEER("🦌", 300, 20, 4, 50),
    RABBIT("🐇", 2, 150, 2, 0.45),
    MOUSE("🖱", 0.05, 500, 1, 0.01),
    GOAT("🐐", 60, 140, 3, 10),
    SHEEP("🐑", 70, 140, 3, 15),
    BOAR("🐗", 400, 50, 2, 50),
    BUFFALO("🐂", 700, 10, 3, 100),
    DUCK("🦆", 1, 200, 3, 0.15),
    CATERPILLAR("🐛", 0.01, 1000, 0, 0),

    // РОСЛИНА
    PLANT("🌿", 1, 200, 0, 0);

    //Фiнальнi поля - базовi характеристики виду

    private final String icon;
    private final double weight; //  вага одного органiзму
    private final int maxOnCell; // Максимум осiб цього виду на клiтину
    private final int speed; // максимальна дистанцiя ходу - клiтинок
    private final double foodRequired;// Скiльки iжи потрiбно для повного насичення

    OrganismType(String icon,double weight, int maxOnCell, int speed, double foodRequired) {
        this.icon = icon;
        this.weight = weight;
        this.maxOnCell = maxOnCell;
        this.speed = speed;
        this.foodRequired = foodRequired;

    }
    public String getIcon(){
        return icon;
    }

    public double getWeight() {
        return weight;
    }
    public int getSpeed() {
        return speed;
    }
    public double getFoodRequired() {
        return foodRequired;
    }
    public int getMaxOnCell() {
        return maxOnCell;
    }

}






















