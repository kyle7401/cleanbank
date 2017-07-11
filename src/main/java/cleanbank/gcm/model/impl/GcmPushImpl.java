package cleanbank.gcm.model.impl;

import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.gcm.model.GcmPush;
import cleanbank.gcm.vo.GcmMulticatResult;
import cleanbank.gcm.vo.GcmPushInfo;
import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Gcm Push Implementation.
 *
 * @author lks21c(lks21c@gmail.com)
 * @since 2014-12-16
 */
//고객용
public class GcmPushImpl implements GcmPush {

//	private String GCM_API_KEY = "AIzaSyApnadCd3eDsVLJ3xX5-pYFBndTMPeCV9U";
//	private String GCM_API_KEY = "AIzaSyBj5vboYyweQdXichfGR5BUBRvjJBZv36I"; // 기존 백민
//	private String GCM_API_KEY = "AIzaSyBlX4vgGAf9Fu_QbWzKvCAPacx59TEEiP4"; // 함진희씨
	private String GCM_API_KEY = "AIzaSyBMowFqPiSlNE6lFHLKqVT6-rTa9BaMfVk"; // 크린뱅크

	private static final int GCM_MULTICAST_LIMIT = 1000;

	private Sender sender = new Sender(GCM_API_KEY);

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public GcmMulticatResult sendPush(GcmPushInfo info) throws GcmMulticastLimitExceededException, IllegalArgumentException, InvalidRequestException, IOException {
		// Validation
		Assert.notNull(info, "info should not be null.");
		Assert.isTrue(info.getData() != null && info.getData().length() > 0, "data should not be Null or empty string.");
		Assert.isTrue(info.getRegIdList() != null && info.getRegIdList().size() > 0, "regIdList should not be Null or empty string.");

		if (info.getRegIdList().size() > GCM_MULTICAST_LIMIT) {
			throw new GcmMulticastLimitExceededException();
		}

		log.debug("GcmPushImpl.sendPush 제목 = {}, 내용 = {}", info.getTitle(), info.getData());

//		푸시를 2개 동시에 보내면 마지막 1개만 보인다?? : 앱에서 처리
		/*if(info.getCollapseKey() == null) {
			info.setCollapseKey("k" + System.currentTimeMillis());
			log.debug("GcmPushImpl.getCollapseKey = {}", info.getCollapseKey());
		}*/

		Message message = new Message.Builder()
				.addData("message", info.getData())
				.addData("title", info.getTitle())
				.delayWhileIdle(info.isDelayWhileIdle())
				.timeToLive(info.getTimeToLive())
				.collapseKey(info.getCollapseKey())
				.build();

//		MulticastResult result = sender.sendNoRetry(message, info.getRegIdList());
		MulticastResult result = sender.send(message, info.getRegIdList(), 2);

		GcmMulticatResult gcmMulticatResult = new GcmMulticatResult(result);
		return gcmMulticatResult;
	}
}
