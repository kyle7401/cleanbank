package cleanbank.controllers.manage;

import cleanbank.Service.TbProductService;
import cleanbank.domain.TbProduct;
import cleanbank.utils.SynapseCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-19.
 */
@Controller
@RequestMapping("/manage/product")
public class ProductController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    //    ----------------------------------------------------------------------------------------------------------------
    private TbProductService tbProductService;

    @Autowired
    public void setTbProductService(TbProductService tbProductService) {
        this.tbProductService = tbProductService;
    }

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 목록
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){

        List<TbProduct> pdLvl1s = tbProductService.getPdLvl1s();
        model.addAttribute("pdLvl1s", pdLvl1s);

        /*List<TbProduct> pdLvl2s = tbProductService.getPdLvl2s();
        model.addAttribute("pdLvl2s", pdLvl2s);*/

        return "manage/product/product_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newLoc(Model model){
        TbProduct product = new TbProduct();

        product.setMode("new");

        model.addAttribute("product", product);
        return "manage/product/product_form";
    }

    /**
     * 저장
     * @param product
     * @return
     */
    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    public String saveProduct(@Valid TbProduct product, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(product.getMode())) { // 신규
            product.setRegiDt(new Date());
            product.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbProductService.saveTbProduct(product);
//        return "redirect:/manage/product/" + product.getId();
        return "fancyclose";
    }

    /**
     * 수정
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/{id}")
    public String showManager(@PathVariable Integer id, Model model){
        TbProduct product = tbProductService.getTbProductById(id);
        product.setMode("edit");

        model.addAttribute("product", product);
        return "manage/product/product_form";
    }

}
