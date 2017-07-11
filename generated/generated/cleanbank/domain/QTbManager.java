package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbManager is a Querydsl query type for TbManager
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbManager extends EntityPathBase<TbManager> {

    private static final long serialVersionUID = 353313630L;

    public static final QTbManager tbManager = new QTbManager("tbManager");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> mgCd = createNumber("mgCd", Integer.class);

    public final DateTimePath<java.util.Date> mgDt = createDateTime("mgDt", java.util.Date.class);

    public final StringPath mgEmail = createString("mgEmail");

    public final StringPath mgNm = createString("mgNm");

    public final StringPath mgPass = createString("mgPass");

    public final StringPath mgTel = createString("mgTel");

    public final StringPath mgType = createString("mgType");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbManager(String variable) {
        super(TbManager.class, forVariable(variable));
    }

    public QTbManager(Path<? extends TbManager> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbManager(PathMetadata<?> metadata) {
        super(TbManager.class, metadata);
    }

}

