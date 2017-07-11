package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbPartner is a Querydsl query type for TbPartner
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbPartner extends EntityPathBase<TbPartner> {

    private static final long serialVersionUID = -1274875783L;

    public static final QTbPartner tbPartner = new QTbPartner("tbPartner");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> ptCd = createNumber("ptCd", Integer.class);

    public final StringPath ptFax = createString("ptFax");

    public final StringPath ptNm = createString("ptNm");

    public final NumberPath<Integer> ptPer = createNumber("ptPer", Integer.class);

    public final StringPath ptTel = createString("ptTel");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbPartner(String variable) {
        super(TbPartner.class, forVariable(variable));
    }

    public QTbPartner(Path<? extends TbPartner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbPartner(PathMetadata<?> metadata) {
        super(TbPartner.class, metadata);
    }

}

