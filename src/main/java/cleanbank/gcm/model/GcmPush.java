package cleanbank.gcm.model;

import com.google.android.gcm.server.InvalidRequestException;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.gcm.vo.GcmMulticatResult;
import cleanbank.gcm.vo.GcmPushInfo;

import java.io.IOException;

/**
 * Gcm Push Interface
 *
 * @author lks21c(lks21c@gmail.com)
 * @since 2014-12-16
 */
//@Service("GcmPush")
public interface GcmPush {
	/**
	 * Push를 보낸다.
	 *
	 * @param {@link GcmPushInfo}
	 * @return {@link GcmMulticatResult}
	 * @throws GcmMulticastLimitExceededException
	 * @throws IllegalArgumentException
	 * @throws InvalidRequestException
	 * @throws IOException
	 */
	public GcmMulticatResult sendPush(GcmPushInfo info) throws GcmMulticastLimitExceededException, IllegalArgumentException, InvalidRequestException, IOException;
}
