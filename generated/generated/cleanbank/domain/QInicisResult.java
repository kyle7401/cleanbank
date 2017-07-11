package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QInicisResult is a Querydsl query type for InicisResult
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInicisResult extends EntityPathBase<InicisResult> {

    private static final long serialVersionUID = -1069352569L;

    public static final QInicisResult inicisResult = new QInicisResult("inicisResult");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath PAmt = createString("PAmt");

    public final StringPath PAuthDt = createString("PAuthDt");

    public final StringPath PAuthNo = createString("PAuthNo");

    public final StringPath PCardIssuerCode = createString("PCardIssuerCode");

    public final StringPath PCardIssuerName = createString("PCardIssuerName");

    public final StringPath PCardMemberNum = createString("PCardMemberNum");

    public final StringPath PCardNum = createString("PCardNum");

    public final StringPath PCardPurchaseCode = createString("PCardPurchaseCode");

    public final StringPath PCardPurchaseName = createString("PCardPurchaseName");

    public final StringPath PFnCd1 = createString("PFnCd1");

    public final StringPath PFnCd2 = createString("PFnCd2");

    public final StringPath PFnNm = createString("PFnNm");

    public final StringPath PIspCardcode = createString("PIspCardcode");

    public final StringPath PMerchantReserved = createString("PMerchantReserved");

    public final StringPath PMid = createString("PMid");

    public final StringPath PMname = createString("PMname");

    public final StringPath PNoti = createString("PNoti");

    public final StringPath POid = createString("POid");

    public final StringPath PPrtcCode = createString("PPrtcCode");

    public final StringPath PRmesg1 = createString("PRmesg1");

    public final StringPath PRmesg2 = createString("PRmesg2");

    public final StringPath PRmesg3 = createString("PRmesg3");

    public final StringPath PStatus = createString("PStatus");

    public final StringPath PTid = createString("PTid");

    public final StringPath PType = createString("PType");

    public final StringPath PUname = createString("PUname");

    public QInicisResult(String variable) {
        super(InicisResult.class, forVariable(variable));
    }

    public QInicisResult(Path<? extends InicisResult> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInicisResult(PathMetadata<?> metadata) {
        super(InicisResult.class, metadata);
    }

}

