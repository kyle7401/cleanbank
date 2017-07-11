package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbPromoLocation is a Querydsl query type for TbPromoLocation
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbPromoLocation extends EntityPathBase<TbPromoLocation> {

    private static final long serialVersionUID = -1743906603L;

    public static final QTbPromoLocation tbPromoLocation = new QTbPromoLocation("tbPromoLocation");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath plCd = createString("plCd");

    public final StringPath plNm = createString("plNm");

    public final NumberPath<Long> poCd = createNumber("poCd", Long.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbPromoLocation(String variable) {
        super(TbPromoLocation.class, forVariable(variable));
    }

    public QTbPromoLocation(Path<? extends TbPromoLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbPromoLocation(PathMetadata<?> metadata) {
        super(TbPromoLocation.class, metadata);
    }

}

