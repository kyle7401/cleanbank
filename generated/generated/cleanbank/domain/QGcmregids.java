package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QGcmregids is a Querydsl query type for Gcmregids
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGcmregids extends EntityPathBase<Gcmregids> {

    private static final long serialVersionUID = -1362860556L;

    public static final QGcmregids gcmregids = new QGcmregids("gcmregids");

    public final StringPath regId = createString("regId");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public QGcmregids(String variable) {
        super(Gcmregids.class, forVariable(variable));
    }

    public QGcmregids(Path<? extends Gcmregids> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGcmregids(PathMetadata<?> metadata) {
        super(Gcmregids.class, metadata);
    }

}

