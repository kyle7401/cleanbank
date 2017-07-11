package cleanbank.controllers.take;

import cleanbank.Service.ITbPictureService;
import cleanbank.domain.TbPicture;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbPictureRepository;
import cleanbank.utils.SynapseCM;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.apache.poi.util.IOUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by hyoseop on 2015-12-24.
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //    config.properties : 품목 리스트의 품목 정보 이미지 업로드 폴더및 http base url
    @Value("${ord_items_img_dir}")
    String ord_items_img_dir;

    @Value("${ord_items_img_url}")
    String ord_items_img_url;

    @Autowired
    private ITbPictureService tbPictureService;

    @Autowired
    private TbPictureRepository tbPictureRepository;

    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map upload(@RequestParam(value = "files[]", required = false) MultipartFile[] files,
                      @RequestParam(value="id", required=true) Long itCd,
                      @RequestParam(value="orCd", required=true) Long orCd) throws IllegalStateException, IOException, ImagingOpException {

        log.debug("업로드 시작됨");

        if(files.length <= 0) {
            log.error("첨부파일 없음");
        } else {
            log.error("첨부파일 갯수 = {}", files.length);
        }

        Map<String, Object> file_list = tbPictureService.uploadImages(files, itCd, orCd);
        return file_list;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public Map list(@And({@Spec(path = "orCd", spec = Equal.class), @Spec(path = "itCd", spec = Equal.class)}) Specification<TbPicture> spec1,
                    @Or({@Spec(path = "delYn", spec = notEqual.class, constVal = "Y"),
                            @Spec(path = "delYn", spec = IsNull.class)}) Specification<TbPicture> spec2,
                    @PageableDefault(sort = {"name"}, size = 1000) Pageable pageable,
                    HttpServletRequest request) {

        log.debug("uploadGet called");

/*        Enumeration em = request.getParameterNames();
        while(em.hasMoreElements()){
            String parameterName = (String)em.nextElement();
            String parameterValue = request.getParameter(parameterName);
            String[] parameterValues = request.getParameterValues(parameterName);
            if(parameterValues != null){
                for(int i=0; i<parameterValues.length; i++){
                    System.out.println("array_" + parameterName + "=" + parameterValues[i]);
                }
            } else {
                System.out.println(parameterName + "=" + parameterValue);
            }
        }*/

        Map<String, Object> files = new HashMap<>();
//        List<TbPicture> list = new ArrayList<TbPicture>();
        List<TbPicture> list = null;

        if (spec1 != null) {
            Specification<TbPicture> spec = Specifications.where(spec1).and(spec2);

            //        List<TbPicture> list = imageDao.list();
//        List<TbPicture> list = StreamSupport.stream(tbPictureService.listAllTbPictures(spec2).spliterator(), false).collect(Collectors.toList());
            list = StreamSupport.stream(tbPictureRepository.findAll(spec, pageable).spliterator(), false).collect(Collectors.toList());

//        TODO : TbPicture class에서 처리 해도 될듯
/*            for (TbPicture image : list) {
                image.setUrl("/file/picture/" + image.getId());
                image.setThumbnailUrl("/file/thumbnail/" + image.getId());
                image.setDeleteUrl("/file/delete/" + image.getId());
                image.setDeleteType("DELETE");
            }*/
        }

        files.put("files", list);
        log.debug("Returning: {}", files);

        return files;
    }

    @RequestMapping(value = "picture/{id}", method = RequestMethod.GET)
    public void picture(@PathVariable("id") TbPicture image, HttpServletResponse response) {
//        Image image = imageDao.get(id);
//        TbPicture image =  tbPictureService.getTbPictureById(id);

        File imageFile = new File(ord_items_img_dir + image.getNewFilename());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            log.error("Could not show picture {} \n {}", image.getId(), e);
        }
    }

    @RequestMapping(value = "thumbnail/{id}", method = RequestMethod.GET)
    public void thumbnail(HttpServletResponse response, @PathVariable("id") TbPicture image) {
//        Image image = imageDao.get(id);

        File imageFile = new File(ord_items_img_dir + image.getThumbnailFilename());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getThumbnailSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            log.error("Could not show thumbnail {} \n {}", image.getId(), e);
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public List delete(@PathVariable("id") TbPicture image) {
//        Image image = imageDao.get(id);

        File imageFile = new File(ord_items_img_dir + image.getNewFilename());
        imageFile.delete();
        File thumbnailFile = new File(ord_items_img_dir + image.getThumbnailFilename());
        thumbnailFile.delete();

//        imageDao.delete(image);
        tbPictureService.deleteTbPicture2(image.getId());

        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        results.add(success);
        return results;
    }

    @RequestMapping(value = "upload_org", method = RequestMethod.POST)
    public Map upload_org(@RequestParam(value = "files[]", required = false) MultipartFile[] files,
                          @RequestParam(value="id", required=true) Long itCd,
                          @RequestParam(value="orCd", required=true) Long orCd) throws IllegalStateException, IOException, ImagingOpException {

        log.debug("업로드 시작됨");

        if(files.length <= 0) {
            log.error("첨부파일 없음");
        } else {
            log.error("첨부파일 갯수 = {}", files.length);
        }

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

                image = tbPictureService.saveTbPicture(image);

/*                image.setUrl("/file/picture/"+ image.getId());
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

}