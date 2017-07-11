package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbMember is a Querydsl query type for TbMember
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbMember extends EntityPathBase<TbMember> {

    private static final long serialVersionUID = 1954725065L;

    public static final QTbMember tbMember = new QTbMember("tbMember");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final StringPath mbAnimal = createString("mbAnimal");

    public final StringPath mbBillinfo = createString("mbBillinfo");

    public final StringPath mbBillkey = createString("mbBillkey");

    public final DateTimePath<java.util.Date> mbBirth = createDateTime("mbBirth", java.util.Date.class);

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public final StringPath mbDeviceToken = createString("mbDeviceToken");

    public final StringPath mbEmail = createString("mbEmail");

    public final StringPath mbHabi = createString("mbHabi");

    public final DateTimePath<java.util.Date> mbJoinDt = createDateTime("mbJoinDt", java.util.Date.class);

    public final StringPath mbKid = createString("mbKid");

    public final StringPath mbLevel = createString("mbLevel");

    public final StringPath mbMarri = createString("mbMarri");

    public final StringPath mbMycode = createString("mbMycode");

    public final StringPath mbNicNm = createString("mbNicNm");

    public final StringPath mbNm = createString("mbNm");

    public final StringPath mbPass = createString("mbPass");

    public final StringPath mbPath = createString("mbPath");

    public final NumberPath<Integer> mbPoint = createNumber("mbPoint", Integer.class);

    public final StringPath mbPush = createString("mbPush");

    public final StringPath mbPushToken = createString("mbPushToken");

    public final StringPath mbSms = createString("mbSms");

    public final StringPath mbStatus = createString("mbStatus");

    public final StringPath mbSucode = createString("mbSucode");

    public final StringPath mbTel = createString("mbTel");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbMember(String variable) {
        super(TbMember.class, forVariable(variable));
    }

    public QTbMember(Path<? extends TbMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbMember(PathMetadata<?> metadata) {
        super(TbMember.class, metadata);
    }

}

