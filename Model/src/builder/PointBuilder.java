package builder;

import jaxb.generated.Location;

import java.awt.*;

public class PointBuilder implements Builder<Location, Point> {
    @Override
    public Point build(Location xmlLocation) {
        return new Point(xmlLocation.getX(), xmlLocation.getY());
    }
}
