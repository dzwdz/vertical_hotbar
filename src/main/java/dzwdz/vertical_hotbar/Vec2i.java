package dzwdz.vertical_hotbar;

public class Vec2i {
    public final int x;
    public final int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i add(Vec2i other) {
        return new Vec2i(x + other.x, y + other.y);
    }

    public Vec2i invertY(int toAdd) {return new Vec2i(x, toAdd - y);}
}
