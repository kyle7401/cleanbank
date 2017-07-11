package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbPoint is a Querydsl query type for TbPoint
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbPoint extends EntityPathBase<TbPoint> {

    private static final long serialVersionUID = 1590141313L;

    public static final QTbPoint tbPoint = new QTbPoint("tbPoint");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public final NumberPath<Long> piCd = createNumber("piCd", Long.class);

    public final DateTimePath<java.util.Date> piChaDt = createDateTime("piChaDt", java.util.Date.class);

    public final StringPath piCharge = createString("piCharge");

    public final NumberPath<Integer> piPoint = createNumber("piPoint", Integer.class);

    public final DateTimePath<java.util.Date> piUseDt = createDateTime("piUseDt", java.util.Date.class);

    public final StringPath plUseMemo = createString("plUseMemo");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbPoint(String variable) {
        super(TbPoint.class, forVariable(variable));
    }

    public QTbPoint(Path<? extends TbPoint> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbPoint(PathMetadata<?> metadata) {
        super(TbPoint.class, metadata);
    }

}

