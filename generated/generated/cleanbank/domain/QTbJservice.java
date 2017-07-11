package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbJservice is a Querydsl query type for TbJservice
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbJservice extends EntityPathBase<TbJservice> {

    private static final long serialVersionUID = -16990854L;

    public static final QTbJservice tbJservice = new QTbJservice("tbJservice");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> jsCnt = createNumber("jsCnt", Integer.class);

    public final StringPath jsImg = createString("jsImg");

    public final StringPath jsMemo = createString("jsMemo");

    public final StringPath jsNm = createString("jsNm");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbJservice(String variable) {
        super(TbJservice.class, forVariable(variable));
    }

    public QTbJservice(Path<? extends TbJservice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbJservice(PathMetadata<?> metadata) {
        super(TbJservice.class, metadata);
    }

}

