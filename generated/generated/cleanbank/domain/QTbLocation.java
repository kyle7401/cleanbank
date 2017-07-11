package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbLocation is a Querydsl query type for TbLocation
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbLocation extends EntityPathBase<TbLocation> {

    private static final long serialVersionUID = -154402268L;

    public static final QTbLocation tbLocation = new QTbLocation("tbLocation");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath loc1 = createString("loc1");

    public final StringPath loc2 = createString("loc2");

    public final StringPath loc3 = createString("loc3");

    public final StringPath loDesc = createString("loDesc");

    public final StringPath name = createString("name");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final StringPath user = createString("user");

    public QTbLocation(String variable) {
        super(TbLocation.class, forVariable(variable));
    }

    public QTbLocation(Path<? extends TbLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbLocation(PathMetadata<?> metadata) {
        super(TbLocation.class, metadata);
    }

}

