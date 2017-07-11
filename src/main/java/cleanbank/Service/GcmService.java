package cleanbank.Service;

import cleanbank.domain.TbMember;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.gcm.model.GcmPush;
import cleanbank.gcm.vo.GcmMulticatResult;
import cleanbank.gcm.vo.GcmPushInfo;
import cleanbank.repositories.TbMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyoseop on 2016-02-19.
 */
@Service
public class GcmService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GcmPush gcmPush;

    @Autowired
    private TbMemberRepository tbMemberRepository;

//    ----------------------------------------------------------------------------------------------------------------

    /**
     * gcm 푸쉬 메세지 보내기 공통(모든 사용자)
     * @param pushTitle
     * @param pushBody
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    public GcmMulticatResult sendPushMsgAll (final String pushTitle, final String pushBody) throws IOException, GcmMulticastLimitExceededException {
        log.error("GcmService->sendPushMsgAll 시작!============================================================");

        List<String> regIdList = new ArrayList<String>();

        List<TbMember> memberList = tbMemberRepository.listAllMembers();

        for(TbMember tbMember : memberList) {
            String pushToken = tbMember.getMbPushToken();
            if(!StringUtils.isEmpty(pushToken) && pushToken.length() > 10) regIdList.add(pushToken);
        }

        log.debug("{} 명의 사용자에게 푸시 메세지 전송!", regIdList.size());

        return this.sendPushMsg(pushTitle, pushBody, regIdList);
    }

    /**
     * gcm 푸쉬 메세지 보내기 공통(단일 사용자)
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    public GcmMulticatResult sendPushMsg (final String pushTitle, final String pushBody, final String deviceToken) throws IOException, GcmMulticastLimitExceededException {
        List<String> regIdList = new ArrayList<String>();
        regIdList.add(deviceToken);
        return this.sendPushMsg(pushTitle, pushBody, regIdList);
    }

    /**
     * gcm 푸쉬 메세지 보내기 공통
     * @param strTitle
     * @param strBody
     * @param regIdList
     * @return
     * @throws IOException
     * @throws GcmMulticastLimitExceededException
     */
    public GcmMulticatResult sendPushMsg (final String strTitle, final String strBody, final List<String> regIdList) throws IOException, GcmMulticastLimitExceededException {

//        List<Gcmregids> tokenList = gcmregidsRepository.findAll2();

//        Push 메세지 전송
        GcmPushInfo info = new GcmPushInfo();

        info.setTitle(strTitle);
        info.setData(strBody);

/*        info.setTitle(notice.getTitle());
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(notice);
        info.setData(jsonInString);

        List<String> regIdList = new ArrayList<String>();

//        regIdList.add("fFFuqZ4AK8Q:APA91bHeqlRxuUvJuQMYestOsdH01w9Hk-SCjLU1HVy9u7fzhnehU1yYoD_ulaLIInoKv7BS-O6osrbAM9YEO0OzgfcWVJSK5zkWh7O8OIpFOyEIgAineLhPbGRWr9mIOtbfiOv11OZ_");
        for (Gcmregids gcmregid : tokenList) {
            regIdList.add(gcmregid.getRegId());
        }*/

        info.setRegIdList(regIdList);

        GcmMulticatResult result = gcmPush.sendPush(info);

        return result;
    }
}
