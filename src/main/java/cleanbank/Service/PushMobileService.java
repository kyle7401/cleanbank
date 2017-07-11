package cleanbank.Service;

import cleanbank.domain.*;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.viewmodel.MoPush;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by hyoseop on 2015-12-08.
 */
public interface PushMobileService {

    ResponseEntity<?> sendDeliStart(final Long orCd) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendReqStart(final Long orCd) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendStart(final Long orCd) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendPromotion(TbPromotion promotion) throws IOException, GcmMulticastLimitExceededException;

    public ResponseEntity<?> sendJoinPromotion(TbMember tbMember, TbPromotion promotion) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendPushMsgAll(MoPush moPush) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendPollMsg(TbMember tbMember) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendSucode(TbMember tbMember) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendJoinPoint(TbMember tbMember) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendMbrPoint(TbMember tbMember, final Integer pushNo, final Integer point) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendPayRequire(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendStatus04(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendCodiModify(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    ResponseEntity<?> sendCodiAssign(TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    ResponseEntity<?> sendDeliComplete(final TbOrder tbOrder) throws IOException, GcmMulticastLimitExceededException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    ResponseEntity<?> sendByMbId(final Integer mbCd, MoPush moPush) throws IOException, GcmMulticastLimitExceededException;

    ResponseEntity<?> sendByMbId(final Integer mbCd, final String pushTitle, final String pushBody) throws IOException, GcmMulticastLimitExceededException;
}
