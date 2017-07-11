package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QTbPromotionUse is a Querydsl query type for TbPromotionUse
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbPromotionUse extends EntityPathBase<TbPromotionUse> {

    private static final long serialVersionUID = 1200913619L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTbPromotionUse tbPromotionUse = new QTbPromotionUse("tbPromotionUse");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public final NumberPath<Long> orCd = createNumber("orCd", Long.class);

    public final StringPath puUse = createString("puUse");

    public final DateTimePath<java.util.Date> puUseDt = createDateTime("puUseDt", java.util.Date.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final QTbPromotion tbPromotion;

    public final StringPath user = createString("user");

    public final StringPath useYn = createString("useYn");

    public QTbPromotionUse(String variable) {
        this(TbPromotionUse.class, forVariable(variable), INITS);
    }

    public QTbPromotionUse(Path<? extends TbPromotionUse> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTbPromotionUse(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTbPromotionUse(PathMetadata<?> metadata, PathInits inits) {
        this(TbPromotionUse.class, metadata, inits);
    }

    public QTbPromotionUse(Class<? extends TbPromotionUse> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tbPromotion = inits.isInitialized("tbPromotion") ? new QTbPromotion(forProperty("tbPromotion")) : null;
    }

}

