package pe.mplescano.mobile.myapp.poc03.dao.iface;

import java.util.List;
import java.util.Map;

/**
 * Created by mplescano on 15/10/2016.
 */
public interface IEntityDao<T> {

    List<T> getEntityList(Map<String, Object> mpParameters);

    long countEntityList(Map<String, Object> mpParameters);
}
