package cleanbank.gcm.model.impl;

import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.gcm.vo.GcmMulticatResult;
import cleanbank.gcm.vo.GcmPushInfo;
import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Gcm Push Implementation.
 *
 * @author lks21c(lks21c@gmail.com)
 * @since 2014-12-16
 */
//코디용
@Service
public class GcmPushImpl2 {

//	private String GCM_API_KEY = "AIzaSyAm3A9uLaOOttgEPsL0ZnZtYdN8eJYdjvw"; // 기존
//	private String GCM_API_KEY = "AIzaSyBlX4vgGAf9Fu_QbWzKvCAPacx59TEEiP4"; // 함진희씨
	private String GCM_API_KEY = "AIzaSyAiMkaMytWvHU6NTr8BL5Sne9cjF8GYcNI"; // 크린뱅크

	private static final int GCM_MULTICAST_LIMIT = 1000;

	private Sender sender = new Sender(GCM_API_KEY);

	private final Logger log = LoggerFactory.getLogger(this.getClass());

//	@Override
	public GcmMulticatResult sendPush(GcmPushInfo info) throws GcmMulticastLimitExceededException, IllegalArgumentException, InvalidRequestException, IOException {
		// Validation
		Assert.notNull(info, "info should not be null.");
		Assert.isTrue(info.getData() != null && info.getData().length() > 0, "data should not be Null or empty string.");
		Assert.isTrue(info.getRegIdList() != null && info.getRegIdList().size() > 0, "regIdList should not be Null or empty string.");

		if (info.getRegIdList().size() > GCM_MULTICAST_LIMIT) {
			throw new GcmMulticastLimitExceededException();
		}

		log.debug("GcmPushImpl.sendPush 제목 = {}, 내용 = {}", info.getTitle(), info.getData());

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
