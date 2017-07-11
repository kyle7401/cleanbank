package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbPartMember is a Querydsl query type for TbPartMember
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbPartMember extends EntityPathBase<TbPartMember> {

    private static final long serialVersionUID = -373466340L;

    public static final QTbPartMember tbPartMember = new QTbPartMember("tbPartMember");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath pmEmail = createString("pmEmail");

    public final StringPath pmNm = createString("pmNm");

    public final StringPath pmPass = createString("pmPass");

    public final StringPath pmTel = createString("pmTel");

    public final NumberPath<Integer> ptCd = createNumber("ptCd", Integer.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbPartMember(String variable) {
        super(TbPartMember.class, forVariable(variable));
    }

    public QTbPartMember(Path<? extends TbPartMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbPartMember(PathMetadata<?> metadata) {
        super(TbPartMember.class, metadata);
    }

}

