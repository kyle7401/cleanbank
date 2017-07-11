package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbPushHistory is a Querydsl query type for TbPushHistory
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbPushHistory extends EntityPathBase<TbPushHistory> {

    private static final long serialVersionUID = 403853131L;

    public static final QTbPushHistory tbPushHistory = new QTbPushHistory("tbPushHistory");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public final StringPath pushTitle = createString("pushTitle");

    public final DateTimePath<java.util.Date> regDt = createDateTime("regDt", java.util.Date.class);

    public QTbPushHistory(String variable) {
        super(TbPushHistory.class, forVariable(variable));
    }

    public QTbPushHistory(Path<? extends TbPushHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbPushHistory(PathMetadata<?> metadata) {
        super(TbPushHistory.class, metadata);
    }

}

