package pe.mplescano.mobile.myapp.poc03.domain;

/**
 * Created by mplescano on 15/10/2016.
 */
public class ImplLabeledEntity implements ILabeledEntity {

    private long mId;

    private String mLabel;

    private String mDetail01;

    private String mDetail02;

    public ImplLabeledEntity(long id, String label) {
        this(id, label, null, null);
    }

    public ImplLabeledEntity(long id, String label, String detail01, String detail02) {
        mId = id;
        mLabel = label;
        mDetail01 = detail01;
        mDetail02 = detail02;
    }

    @Override
    public String getLabel() {
        return mLabel;
    }

    @Override
    public String getDetail01() {
        return mDetail01;
    }

    @Override
    public String getDetail02() {
        return mDetail02;
    }

    @Override
    public long getId() {
        return mId;
    }

}
