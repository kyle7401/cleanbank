package cleanbank.controllers;

import cleanbank.repositories.TbCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;

/**
 * Created by hyoseop on 2015-12-22.
 */
@RepositoryRestController
public class test3Controller {

    @Autowired
    private TbCodeRepository repository;

    /*@Autowired
    public ScannerController(ScannerRepository repo) {
        repository = repo;
    }*/

/*    @RequestMapping(method = GET, value = "/test3/list1")
    public
    @ResponseBody
    ResponseEntity<?> getCodess() {
        Iterable<TbCode> codes = repository.findAll();

        //
        // do some intermediate processing, logging, etc. with the producers
        //

        Resources<TbCode> resources = new Resources<TbCode>(codes);

        resources.add(linkTo(methodOn(test3Controller.class).getCodess()).withSelfRel());

        // add other links as needed

        return ResponseEntity.ok(resources);
    }*/

/*    @RequestMapping(method = GET, value = "/test3/list1")
    public @ResponseBody
    ResponseEntity<?> getCodess() {
        Iterable<TbCode> codes = repository.findAll();

        //
        // do some intermediate processing, logging, etc. with the producers
        //

        Resources<TbCode> resources = new Resources<TbCode>(codes);

        resources.add(linkTo(methodOn(test3Controller.class).getCodess()).withSelfRel());

        // add other links as needed

        return ResponseEntity.ok(resources);
    }*/
}
