package cleanbank.Service;

import cleanbank.domain.TbPicture;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbPictureService {
    Iterable<TbPicture> listAllTbPictures();

    TbPicture getTbPictureById(Integer id);

    TbPicture saveTbPicture(TbPicture TbPicture);

    void deleteTbPicture(Integer id);

    //    이하 추가
    void deleteTbPicture2(Integer id);

//    List<TbPicture> getPictureList(long orCd, long itCd);

    TbPicture getPictureInfo(long orCd, long itCd, long ptCd);

    Map uploadImages(MultipartFile[] files, Long itCd, Long orCd);

//    List<?> getPictureCds(Integer mbCd);
}
