package pe.mplescano.mobile.myapp.poc03.dao.iface;

import java.util.List;
import java.util.Map;

import pe.mplescano.mobile.myapp.poc03.domain.PointLocation;

/**
 * Created by mplescano on 04/10/2016.
 */
public interface PointLocationDao {

    PointLocation getPointLocation(long id);

    long countPointLocationList(Map<String, Object> mpParameters);

    int deletePointLocation(long id);

    List<PointLocation> getPointLocationList(Map<String, Object> mpParams);

    void insertPointLocation(PointLocation pointLocation);

    void updatePointLocation(PointLocation pointLocation);

    void savePointLocation(PointLocation pointLocation);
}
