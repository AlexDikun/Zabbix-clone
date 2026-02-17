package su.dikunia.zabbix_clone.testutils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public final class GeometryTestUtils {

    private static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory();

    private GeometryTestUtils() {}

    public static Point point(double lon, double lat) {
        Point p = GEOMETRY_FACTORY.createPoint(
            new Coordinate(lon, lat)
        );
        p.setSRID(4326);
        return p;
    }
}
