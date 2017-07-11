package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbTraceInfo is a Querydsl query type for TbTraceInfo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbTraceInfo extends EntityPathBase<TbTraceInfo> {

    private static final long serialVersionUID = 1429637892L;

    public static final QTbTraceInfo tbTraceInfo = new QTbTraceInfo("tbTraceInfo");

    public final StringPath addr1 = createString("addr1");

    public final NumberPath<Integer> epCd = createNumber("epCd", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> orCd = createNumber("orCd", Long.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath tiLati = createString("tiLati");

    public final StringPath tiLong = createString("tiLong");

    public QTbTraceInfo(String variable) {
        super(TbTraceInfo.class, forVariable(variable));
    }

    public QTbTraceInfo(Path<? extends TbTraceInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbTraceInfo(PathMetadata<?> metadata) {
        super(TbTraceInfo.class, metadata);
    }

}

