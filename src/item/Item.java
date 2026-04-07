package item;

public abstract class Item {

    String name;
    int price;
    int level;
    Type type;

    protected Item(String name, int price, int level, Type type) {
        this.name = name;
        this.price = price;
        this.level = level;
        this.type = type;
    }

    public abstract void print();

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getLevel() {
        return level;
    }
}