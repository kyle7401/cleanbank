package cleanbank.Service;

import cleanbank.domain.TbFaq;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbFaqService {
    Iterable<TbFaq> listAllTbFaqs();

    TbFaq getTbFaqById(Integer id);

    TbFaq saveTbFaq(TbFaq TbFaq);

    void deleteTbFaq(Integer id);

    //    이하 추가
    void deleteTbFaq2(Integer id);

//    List<?> getFaqCds();
}
