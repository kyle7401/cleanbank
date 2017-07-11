package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbMemberBillinfo is a Querydsl query type for TbMemberBillinfo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbMemberBillinfo extends EntityPathBase<TbMemberBillinfo> {

    private static final long serialVersionUID = -1230211810L;

    public static final QTbMemberBillinfo tbMemberBillinfo = new QTbMemberBillinfo("tbMemberBillinfo");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath mbBiilkey = createString("mbBiilkey");

    public final StringPath mbBillinfo = createString("mbBillinfo");

    public final StringPath mbCardCd = createString("mbCardCd");

    public final StringPath mbCardNm = createString("mbCardNm");

    public final StringPath mbCardNo = createString("mbCardNo");

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public QTbMemberBillinfo(String variable) {
        super(TbMemberBillinfo.class, forVariable(variable));
    }

    public QTbMemberBillinfo(Path<? extends TbMemberBillinfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbMemberBillinfo(PathMetadata<?> metadata) {
        super(TbMemberBillinfo.class, metadata);
    }

}

