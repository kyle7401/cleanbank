package cleanbank.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by hyoseop on 2015-11-05.
 */
@Component
public class ProductLoader implements ApplicationListener<ContextRefreshedEvent> {

//    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /*private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }*/

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        /*Product shirt = new Product();
        shirt.setDescription("Spring Framework Guru Shirt");
        shirt.setPrice(new BigDecimal("18.95"));
        shirt.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
        shirt.setProductId("235268845711068308");
        productRepository.save(shirt);

        log.info("\n\n########### Saved Shirt - id: " + shirt.getId());
        log.debug("\n\n########### 저장된 Shirt - id : {}", shirt.getId().toString());
        log.trace("Entering application.");
        log.error("\n\n\n######################## 에러 메세지 = {}", "XXXXXXX");

        Product mug = new Product();
        mug.setDescription("Spring Framework Guru Mug");
        mug.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_coffee_mug-r11e7694903c348e1a667dfd2f1474d95_x7j54_8byvr_512.jpg");
        mug.setProductId("168639393495335947");
        productRepository.save(mug);

        log.info("Saved Mug - id:" + mug.getId());*/
    }
}