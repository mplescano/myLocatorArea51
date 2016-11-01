package pe.mplescano.mobile.myapp.poc03.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pe.mplescano.mobile.myapp.poc03.dao.iface.IEntityDao;
import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;
import pe.mplescano.mobile.myapp.poc03.domain.ImplLabeledEntity;
import pe.mplescano.mobile.myapp.poc03.domain.PointLocation;
import pe.mplescano.mobile.myapp.poc03.support.Utils;

/**
 * Created by mplescano on 15/10/2016.
 */
public class PointLocationEntityDao implements IEntityDao<ImplLabeledEntity> {

    private PointLocationDao mpointLocationDao;

    public PointLocationEntityDao(PointLocationDao pointLocationDao) {
        mpointLocationDao = pointLocationDao;
    }

    @Override
    public List<ImplLabeledEntity> getEntityList(Map<String, Object> mpParameters) {
        List<ImplLabeledEntity> lstResult = new ArrayList<>();

        for (PointLocation pointLocation : mpointLocationDao.getPointLocationList(mpParameters)) {
            ImplLabeledEntity implLabeledEntity = new ImplLabeledEntity(pointLocation.getId(),
                    Utils.formatDate(pointLocation.getDateCreation(), "EE MMM yyyy-MM-dd hh:mm a"),
                    pointLocation.getAddress(), null);
            lstResult.add(implLabeledEntity);
        }

        return lstResult;
    }

    @Override
    public long countEntityList(Map<String, Object> mpParameters) {
        return mpointLocationDao.countPointLocationList(mpParameters);
    }
}
