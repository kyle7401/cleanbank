package cleanbank.Service;

import cleanbank.domain.TbPicture;
import cleanbank.repositories.TbPictureRepository;
import cleanbank.utils.SynapseCM;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbPictureService implements ITbPictureService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TbPictureRepository tbPictureRepository;

    @Override
    public Iterable<TbPicture> listAllTbPictures() {
        return tbPictureRepository.findAll();
    }

    @Override
    public TbPicture getTbPictureById(Integer id) {
        return tbPictureRepository.findOne(id);
    }

    @Override
    public TbPicture saveTbPicture(TbPicture TbPicture) {
        return tbPictureRepository.save(TbPicture);
    }

    @Override
    public void deleteTbPicture(Integer id) {
        tbPictureRepository.delete(id);
    }

//    이하 추가

    @Value("${ord_items_img_dir}")
    String ord_items_img_dir;

    @Value("${ord_items_img_url}")
    String ord_items_img_url;

    @Autowired
    private SynapseCM synapseCM;

    /*@Autowired
    private ITbPictureService tbPictureService;*/

    @Override
    public Map uploadImages(MultipartFile[] files, Long itCd, Long orCd) {
        List<TbPicture> list = new LinkedList<>();

        for (MultipartFile file : files) {
            log.debug("업로드 파일명 = {}", file.getOriginalFilename());

            String newFilenameBase = UUID.randomUUID().toString();
//            String originalFileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            String newFilename = newFilenameBase + originalFileExtension;
            String storageDirectory = ord_items_img_dir;
            String contentType = file.getContentType();

//            File newFile = new File(storageDirectory + "/" + newFilename);

            try {
                /*file.transferTo(newFile);

                BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 290);*/

                String strUploadNm = SynapseCM.imgUpload(file, ord_items_img_dir, newFilenameBase);
                File newFile = new File(ord_items_img_dir + strUploadNm);

                if(!newFile.canRead()) {
                    log.error("업로드 이미지 접근 불가!!!");
                }

//                BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 290);
                BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 80);
                String thumbnailFilename = newFilenameBase + "-thumbnail.png";
                File thumbnailFile = new File(storageDirectory + "/" + thumbnailFilename);
                ImageIO.write(thumbnail, "png", thumbnailFile);

                TbPicture image = new TbPicture();
                image.setName(file.getOriginalFilename());
                image.setThumbnailFilename(thumbnailFilename);
                image.setNewFilename(newFile.getName());
                image.setContentType(contentType);
                image.setSize(file.getSize());
                image.setThumbnailSize(thumbnailFile.length());
//                image = imageDao.create(image);

                image.setItCd(itCd);
                image.setOrCd(orCd);

                image = this.saveTbPicture(image);

                /*image.setUrl("/file/picture/"+ image.getId());
                image.setThumbnailUrl("/file/thumbnail/"+ image.getId());
                image.setDeleteUrl("/file/delete/"+ image.getId());
                image.setDeleteType("DELETE");*/

                list.add(image);

            } catch(IOException e) {
                log.error("{} 파일 업로드 실패 \n {}", file.getOriginalFilename(), e);
            }
        }

        Map<String, Object> file_list = new HashMap<>();
        file_list.put("files", list);
        return file_list;
    }

    @Override
    public void deleteTbPicture2(Integer id) {
        TbPicture picture = tbPictureRepository.findOne(id);
        picture.setDelYn("Y");
        tbPictureRepository.save(picture);
    }

/*    @Override
    public List<TbPicture> getPictureList(long orCd, long itCd) {
        return tbPictureRepository.getPictureList(orCd, itCd);
    }*/

    @Override
    public TbPicture getPictureInfo(long orCd, long itCd, long ptCd) {
        return tbPictureRepository.getPictureInfo(orCd, itCd, ptCd);
    }


/*    @Override
    public List<?> getPictureCds(Integer mbCd) {
        return tbPictureRepository.getPictureCds(mbCd);
    }*/


    /**
     * 상위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbPicture> getPdLvl1s() {
        return tbPictureRepository.getPdLvl1s();
    }*/

    /**
     * 중위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbPicture> getPdLvl2s() {
        return tbPictureRepository.getPdLvl2s();
    }*/

    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbPicture as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

/*    @Override
    public List<String> getcdGps() {
        return tbPictureRepository.getcdGps();
    }*/

/*    @Override
    public List<TbPicture> findByEpEmail(String epEmail) {
        return tbPictureRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbPicture> findByBnCd(Integer bnCd) {
        return TbPictureRepository.findByBnCd(bnCd);
    }*/
}
