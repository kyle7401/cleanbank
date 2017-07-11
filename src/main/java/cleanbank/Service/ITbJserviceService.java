package cleanbank.Service;

import cleanbank.domain.TbJservice;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbJserviceService {
    Iterable<TbJservice> listAllTbJservices();

    TbJservice getTbJserviceById(Long id);

    TbJservice saveTbJservice(TbJservice TbJservice);

    void deleteTbJservice(Long id);

    //    이하 추가
    void deleteTbJservice2(Long id);

//    List<?> getJserviceCds();
}
