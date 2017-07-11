package cleanbank.Service;

import cleanbank.domain.TbPoint;
import cleanbank.repositories.TbPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbPointService implements ITbPointService {
    @Autowired
    private TbPointRepository tbPointRepository;


    public TbPoint savePointUse(TbPoint tbPoint) {
        tbPoint.setDelYn("N");
        tbPoint.setEvtNm("신규");
        tbPoint.setRegiDt(new Date());
        tbPoint.setUser("모바일");

        tbPoint = tbPointRepository.save(tbPoint);
        return tbPoint;
    }
}
