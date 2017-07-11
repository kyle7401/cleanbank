package cleanbank.gcm.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;*/
import cleanbank.gcm.model.GcmPush;
import cleanbank.gcm.model.impl.GcmPushImpl;
import cleanbank.gcm.vo.GcmMulticatResult;
import cleanbank.gcm.vo.GcmPushInfo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { test1.TestConfig.class }, loader = AnnotationConfigContextLoader.class)*/
public class test1 {

	@Resource
	GcmPush gcmPush;

	private String data;

	private List<String> regIdList = new ArrayList<String>();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws Exception {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		String content = "This is Gcm Test. " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + seconds;
		data = content;
//		data = "{\"val\":\"\",\"content\":\"" + content  + "}";
//		data = "{\"title\":\"\",\"message\":\"" + content  + "}";
	}

	@Test
	public void testSendPush() throws Exception {
//		String REGISTRATION_ID = "SAMPLE REG ID";
		/*String content = "This is Gcm Test.";
		String data = "{\"val\":\"\",\"content\":\"" + content  + "}";*/

		GcmPushInfo info = new GcmPushInfo();

		info.setData(data);
		info.setTitle("cleanbank.gcm 테스트 제목1!");

		regIdList.add("ebsW7AmXBS4:APA91bF0mcR5KAmH64pRgN2eHPL6g4h4MIDRLn_bn51g6lAwHUmyUyPtBtIcP587bFFJ0vN62gHtcR7k_PCrDkytTcAYVn4Dw6W7FkiUG7fcg6BoynwjizkfRkOq4DkhA9sTMQv9E4KM");
		info.setRegIdList(regIdList);

		GcmMulticatResult result = gcmPush.sendPush(info);
	}

	@Configuration
	static class TestConfig {

		@Bean
		public GcmPush gcmPush() {
			GcmPushImpl pushImpl = new GcmPushImpl();
			return pushImpl;
		}
	}
}
