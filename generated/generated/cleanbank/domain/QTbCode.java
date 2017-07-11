package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbCode is a Querydsl query type for TbCode
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbCode extends EntityPathBase<TbCode> {

    private static final long serialVersionUID = 1852022748L;

    public static final QTbCode tbCode = new QTbCode("tbCode");

    public final StringPath cdGp = createString("cdGp");

    public final StringPath cdIt = createString("cdIt");

    public final StringPath cdNm = createString("cdNm");

    public final NumberPath<Integer> cdSort = createNumber("cdSort", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbCode(String variable) {
        super(TbCode.class, forVariable(variable));
    }

    public QTbCode(Path<? extends TbCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbCode(PathMetadata<?> metadata) {
        super(TbCode.class, metadata);
    }

}

