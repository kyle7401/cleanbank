package cleanbank.viewmodel;

import cleanbank.domain.TbMember;
import org.springframework.data.rest.core.config.Projection;

/**
 * Created by hyoseop on 2015-12-30.
 *
 * http://docs.spring.io/spring-data/rest/docs/current/reference/html/
 *
 * @Projection(name = "noAddresses", types = { Person.class })
 */

@Projection(types = { TbMember.class })

public interface moMember {
    String getMbNicNm();
}
