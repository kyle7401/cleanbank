package cleanbank.utils;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hyoseop on 2015-11-13.
 * https://bitbucket.org/leonate/jxls-demo
 * http://syaku.tistory.com/301
 */
public class ExcelView extends AbstractExcelView {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void buildExcelDocument(Map<String, Object> model, org.apache.poi.hssf.usermodel.HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment; filename=excel99.xls");

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd_HHmm", Locale.KOREA);
        String today = dateFormat.format(new Date());

        String ExcelFileName = model.get("out_fname") +"_"+ today+ ".xls";
        response.setHeader( "Content-disposition", "attachment;filename=" + URLEncoder.encode(ExcelFileName, "UTF-8"));

        OutputStream os = null;
        InputStream is = null;

        try {
//            List<Employee> employees = generateSampleEmployeeData();
//            List<GoodsVO> goods = (List<GoodsVO>) model.get("goodsList");
//            List<Employee> employees = (List<Employee>) model.get("data");
            List<?> list = (List<?>) model.get("list");

            // 엑셀 템플릿 파일이 존재하는 위치 (classpath 하위)
//            is = new ClassPathResource("/excel.xls").getInputStream();
            is = new ClassPathResource((String) model.get("in_fname")).getInputStream();

            os = response.getOutputStream();
            Context context = new Context();
            context.putVar("list", list);

            JxlsHelper.getInstance().processTemplate(is, os, context);
//            http://jxls.sourceforge.net/samples/excel_formulas.html
//            JxlsHelper.getInstance().processTemplateAtCell(is, os, context, "Result!A2");

            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if(os != null) try { os.close(); } catch (IOException e) { }
            if(is != null) try { is.close(); } catch (IOException e) { }
        }
    }

    /*@Override
    protected void buildExcelDocument(Map<String, Object> model, org.apache.poi.hssf.usermodel.HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=excel99.xls");

        OutputStream os = null;
        InputStream is = null;

        try {
            // 엑셀 템플릿 파일이 존재하는 위치 (classpath 하위)
            is = new ClassPathResource("/excel.xls").getInputStream();
            os = response.getOutputStream();

            *//*XLSTransformer transformer = new XLSTransformer();
            Workbook excel = transformer.transformXLS(is, modal);
            excel.write(os);*//*
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if(os != null) try { os.close(); } catch (IOException e) { }
            if(is != null) try { is.close(); } catch (IOException e) { }
        }
    }*/
}