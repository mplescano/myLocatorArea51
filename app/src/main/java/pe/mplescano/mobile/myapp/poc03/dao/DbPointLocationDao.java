package pe.mplescano.mobile.myapp.poc03.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;
import pe.mplescano.mobile.myapp.poc03.domain.PointLocation;
import pe.mplescano.mobile.myapp.poc03.support.Utils;

/**
 * Created by mplescano on 03/10/2016.
 */
public class DbPointLocationDao implements PointLocationDao {

    private DatabaseHandler databaseHandler;

    public DbPointLocationDao(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public PointLocation getPointLocation(long id) {
        Map<String, Object> mpParameters = new HashMap<>();
        mpParameters.put("id", Long.valueOf(id));
        List<PointLocation> lstResult = getPointLocationList(mpParameters);

        if (lstResult.size() == 0) {
            return null;
        }
        else if (lstResult.size() == 1) {
            return lstResult.get(0);
        }
        else {
            throw new IllegalStateException("it was expected only one record but it was obtained "
                    + lstResult.size() + " records");
        }
    }

    public long countPointLocationList(Map<String, Object> mpParameters) {
        StringBuilder strWhereQuery = new StringBuilder("");
        StringBuilder strOrder = new StringBuilder("");
        StringBuilder strLimit = new StringBuilder("");
        List<String> arrParams = new ArrayList<>();

        processParameters(mpParameters, strWhereQuery, strOrder, strLimit, arrParams);

        long count = DatabaseUtils.queryNumEntries(databaseHandler.getReadableDatabase(),
                "point_location", strWhereQuery.toString(),
                arrParams.toArray(new String[arrParams.size()]));

        return count;
    }

    public int deletePointLocation(long id) {
        Map<String, Object> mpParameters = new HashMap<>();
        mpParameters.put("id", Long.valueOf(id));
        StringBuilder strWhereQuery = new StringBuilder("");
        List<String> arrParams = new ArrayList<>();

        processParameters(mpParameters, strWhereQuery, null, null, arrParams);
        return databaseHandler.getWritableDatabase().delete("point_location", strWhereQuery.toString(),
                arrParams.toArray(new String[arrParams.size()]));
    }

    public List<PointLocation> getPointLocationList(Map<String, Object> mpParameters) {
        List<PointLocation> lstResult = new ArrayList<>();

        StringBuilder strWhereQuery = new StringBuilder("");
        StringBuilder strOrder = new StringBuilder("");
        StringBuilder strLimit = new StringBuilder("");
        List<String> arrParams = new ArrayList<>();

        processParameters(mpParameters, strWhereQuery, strOrder, strLimit, arrParams);

        Cursor curQuery = null;

        try {
            curQuery = databaseHandler.getReadableDatabase().query(false, "point_location",
                    null, strWhereQuery.toString(), arrParams.toArray(new String[arrParams.size()]),
                    null, null, strOrder.toString(), strLimit.toString());

            while (curQuery.moveToNext()) {
                PointLocation pointLocation = new PointLocation();
                pointLocation.setId(curQuery.getLong(curQuery.getColumnIndex("_id")));
                pointLocation.setAddress(curQuery.getString(curQuery.getColumnIndex("address")));
                pointLocation.setDescription(curQuery.getString(curQuery.getColumnIndex("description")));
                pointLocation.setLongitude(curQuery.getDouble(curQuery.getColumnIndex("longitude")));
                pointLocation.setLatitude(curQuery.getDouble(curQuery.getColumnIndex("latitude")));
                pointLocation.setDateCreation(Utils.parseDateUtc(curQuery.getString(curQuery.getColumnIndex("dateCreation"))));
                pointLocation.setDateModification(Utils.parseDateUtc(curQuery.getString(curQuery.getColumnIndex("dateModification"))));

                lstResult.add(pointLocation);
            }
        }
        finally {
            if (curQuery != null) {
                curQuery.close();
            }
        }

        return lstResult;
    }

    private void processParameters(Map<String, Object> mpParameters, StringBuilder strWhereQuery,
                                   StringBuilder strOrder, StringBuilder strLimit,
                                   List<String> arrParams) {
        if (mpParameters.get("id") != null) {
            if (!"".equals(strWhereQuery.toString())) {
                strWhereQuery.append(" AND ");
            }
            strWhereQuery.append("_id = ?");
            arrParams.add(String.valueOf(mpParameters.get("id")));
        }

        if (mpParameters.get("dateCreationIni") != null) {
            if (!"".equals(strWhereQuery.toString())) {
                strWhereQuery.append(" AND ");
            }
            strWhereQuery.append("dateCreation >= ?");
            arrParams.add(Utils.formatDateUtc((Date) mpParameters.get("dateCreationIni"), "yyyy-MM-dd 00:00:00"));
        }

        if (mpParameters.get("dateCreationEnd") != null) {
            if (!"".equals(strWhereQuery.toString())) {
                strWhereQuery.append(" AND ");
            }
            strWhereQuery.append("dateCreation <= ?");
            arrParams.add(Utils.formatDateUtc((Date) mpParameters.get("dateCreationEnd"), "yyyy-MM-dd 23:59:59"));
        }

        if (mpParameters.get("descriptionSearch") != null) {
            if (!"".equals(strWhereQuery.toString())) {
                strWhereQuery.append(" AND ");
            }
            strWhereQuery.append("description like ?");

            arrParams.add("%" + String.valueOf(mpParameters.get("id")) + "%");
        }

        if (strOrder != null) {
            if (mpParameters.get("dateCreationOrder") != null) {
                //mpParameters.get("dateCreationOrder") asc|desc
                if (!"".equals(strOrder.toString())) {
                    strOrder.append(" , ");
                }
                strOrder.append(" dateCreation " + mpParameters.get("dateCreationOrder"));
            }
        }

        if (strLimit != null) {
            if (mpParameters.get("limitQuery") != null) {
                if (mpParameters.get("offsetQuery") != null) {
                    strLimit.append(String.valueOf(mpParameters.get("offsetQuery")));
                    strLimit.append(",");
                }
                strLimit.append(String.valueOf(mpParameters.get("limitQuery")));
            }
        }
    }

    @Override
    public void insertPointLocation(PointLocation pointLocation) {
        if (pointLocation.getId() == 0 ) {
            savePointLocation(pointLocation);
        }
        else {
            throw new IllegalArgumentException("Id has to be zero");
        }
    }

    @Override
    public void updatePointLocation(PointLocation pointLocation) {
        if (pointLocation.getId() > 0) {
            savePointLocation(pointLocation);
        }
        else {
            throw new IllegalArgumentException("Id has to be major than zero");
        }
    }

    @Override
    public void savePointLocation(PointLocation pointLocation) {
        /*PointLocation dbPointLocation = null;
        if (pointLocation.getId() == 0) {
            dbPointLocation = new PointLocation();
        }
        else {
            dbPointLocation = getPointLocation(pointLocation.getId());
        }*/

        ContentValues pairValues = new ContentValues();
        SQLiteDatabase db = databaseHandler.getWritableDatabase();

        pairValues.put("address", pointLocation.getAddress());
        pairValues.put("description", pointLocation.getDescription());
        pairValues.put("latitude", pointLocation.getLatitude());
        pairValues.put("longitude", pointLocation.getLongitude());
        pairValues.put("dateCreation", Utils.formatDateUtc(pointLocation.getDateCreation()));
        pairValues.put("dateModification", Utils.formatDateUtc(pointLocation.getDateModification()));

        if (pointLocation.getId() == 0) {
            long insertedId = db.insert("point_location", null, pairValues);
            pointLocation.setId(insertedId);
        }
        else {
            db.update("point_location", pairValues, "_id = ?", new String[]{String.valueOf(pointLocation.getId())});
        }

    }


}
