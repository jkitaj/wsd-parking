package pl.pw.wsd.wsdparking.city;

public class Region {
    private final int leftX;
    private final int width;
    private final int topY;
    private final int height;

    public Region(int leftX, int width, int topY, int height) {
        this.leftX = leftX;
        this.width = width;
        this.topY = topY;
        this.height = height;
    }

    public int getLeftX() {
        return leftX;
    }

    public int getWidth() {
        return width;
    }

    public int getTopY() {
        return topY;
    }

    public int getHeight() {
        return height;
    }

    public Position center() {
        return new Position(leftX + (width / 2), topY + (height / 2));
    }

    @Override
    public String toString() {
        return "Region{" +
                "leftX=" + leftX +
                ", width=" + width +
                ", topY=" + topY +
                ", height=" + height +
                '}';
    }
}
