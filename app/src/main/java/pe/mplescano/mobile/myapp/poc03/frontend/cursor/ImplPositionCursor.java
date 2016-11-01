package pe.mplescano.mobile.myapp.poc03.frontend.cursor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.mplescano.mobile.myapp.poc03.dao.iface.IEntityDao;
import pe.mplescano.mobile.myapp.poc03.domain.IEntity;

/**
 * Created by mplescano on 15/10/2016.
 */
public class ImplPositionCursor<T extends IEntity> implements PositionCursor {

    private Map<String, Object> mMpParameters;

    private IEntityDao<T> mEntityDao;

    private long totalPage;

    private long totalCount;

    private long mItemsByPage;

    /**
     * it starts from 1
     */
    private long currentPage;

    private List<T> currentBatch;

    public ImplPositionCursor(Map<String, Object> mpParameters, IEntityDao<T> entityDao,
                              long itemsByPage) {
        mMpParameters = mpParameters;
        mEntityDao = entityDao;
        mItemsByPage = itemsByPage;
        currentPage = 1;
        update();
    }

    @Override
    public long getCount() {
        return totalCount;
    }

    @Override
    public T getItem(long position) {
        if (position < totalCount) {
            long positionInItemPage = 0;
            if (position < ((currentPage - 1) * mItemsByPage) ||
                    position >= (currentPage * mItemsByPage)) {
                currentPage = (position / mItemsByPage) + 1;
                Map<String, Object> mpParams = new HashMap<>();
                mpParams.putAll(mMpParameters);
                mpParams.put("limitQuery", mItemsByPage);
                mpParams.put("offsetQuery", (currentPage - 1) * mItemsByPage);
                currentBatch = mEntityDao.getEntityList(mpParams);
            }
            if (currentPage - 1 > 0) {
                positionInItemPage = position % ((currentPage - 1) * mItemsByPage);
            }
            else {
                positionInItemPage = position;
            }
            return currentBatch.get((int) positionInItemPage);
        }
        return null;
    }

    @Override
    public long getItemId(long position) {
        T item = getItem(position);
        if (item == null) {
            return 0;
        }
        return item.getId();
    }

    public void update() {
        totalCount = mEntityDao.countEntityList(mMpParameters);

        if (totalCount % mItemsByPage > 0) {
            totalPage = (totalCount / mItemsByPage) + 1;
        }
        else {
            totalPage = (totalCount / mItemsByPage);
        }

        Map<String, Object> mpParams = new HashMap<>();
        mpParams.putAll(mMpParameters);
        mpParams.put("limitQuery", mItemsByPage);
        mpParams.put("offsetQuery", (currentPage - 1) * mItemsByPage);
        currentBatch = mEntityDao.getEntityList(mpParams);

    }
}
