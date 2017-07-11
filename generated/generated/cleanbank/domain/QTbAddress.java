package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbAddress is a Querydsl query type for TbAddress
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbAddress extends EntityPathBase<TbAddress> {

    private static final long serialVersionUID = -1629638747L;

    public static final QTbAddress tbAddress = new QTbAddress("tbAddress");

    public final NumberPath<Integer> adCd = createNumber("adCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath mbAddr1 = createString("mbAddr1");

    public final StringPath mbAddr2 = createString("mbAddr2");

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public final StringPath mbLat = createString("mbLat");

    public final StringPath mbLng = createString("mbLng");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbAddress(String variable) {
        super(TbAddress.class, forVariable(variable));
    }

    public QTbAddress(Path<? extends TbAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbAddress(PathMetadata<?> metadata) {
        super(TbAddress.class, metadata);
    }

}

