package cleanbank.Service;

import cleanbank.domain.TbEmployee;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbEmployeeService {
    Iterable<TbEmployee> listAllTbEmployees();

    TbEmployee getTbEmployeeById(Integer id);

    TbEmployee saveTbEmployee(TbEmployee TbEmployee);

    void deleteTbEmployee(Integer id);

    //    이하 추가
//    List<TbEmployee> findByEpEmail(String epEmail);
    TbEmployee findByEpEmail(String epEmail);

    void deleteTbEmployee2(Integer id);

    List<?> getEmpCds();

    //    그리드및 엑셀용 검색(조건추가)
    List<?> getList(
            final String bncd,
            /*final String from,
            final String to,*/
            final String keyword,
            final String condition,
            final String epPart
    );
}
