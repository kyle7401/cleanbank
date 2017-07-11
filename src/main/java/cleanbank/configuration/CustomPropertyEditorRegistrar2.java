package cleanbank.configuration;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/validation.html#core-convert-Spring-config
 * Using PropertyEditorRegistrars
 *
 * Created by hyoseop on 2015-11-10.
 */
@Service
public class CustomPropertyEditorRegistrar2 implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
//        String.class
        registry.registerCustomEditor(String.class, new StringTrimmerEditor(false));

        //        Timestamp.class가 적용 안되고 아래 Date.class에  HH:mm을 적용하면 적용이 된다
/*        // 		CustomDateEditor(this.dateFormat = dateFormat, this.allowEmpty = allowEmpty,  this.exactDateLength = -1)
//        registry.registerCustomEditor(Timestamp.class, new CustomDateEditor(Timestamp.class));

//        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        SimpleDateFormat dtFormat2 = new SimpleDateFormat("yyyy-mm-dd HH:mm");
        dtFormat2.setLenient(false);
//        registry.registerCustomEditor(Timestamp.class, new CustomDateEditor(dtFormat2, true));

//        registry.registerCustomEditor(java.sql.Timestamp.class, new TimestampPropertyEditor());
        registry.registerCustomEditor(TemporalType.TIMESTAMP.getClass(), new CustomDateEditor(dtFormat2, true));
        registry.registerCustomEditor(java.sql.Time.class, new CustomDateEditor(dtFormat2, true));
        registry.registerCustomEditor(java.sql.Timestamp.class, new CustomDateEditor(dtFormat2, true));*/

//        JPA로 date와 datetime을 동시에 처리하는 방법을 몰라서 CustomPropertyEditorRegistrar를 2개 만들어서 Controller마다 선택하여 호출
//        Date.class
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setLenient(false);

        registry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        registry.registerCustomEditor(java.sql.Date.class, new CustomDateEditor(dateFormat, true));
    }

    /*@Override
    protected AbstractPropertyBindingResult getInternalBindingResult() {
        AbstractPropertyBindingResult bindingResult = super.getInternalBindingResult();

        // by rose
        PropertyEditorRegistry registry = bindingResult.getPropertyEditorRegistry();
        registry.registerCustomEditor(Date.class, new JSpinner.DateEditor(Date.class));
        registry.registerCustomEditor(java.sql.Date.class, new JSpinner.DateEditor(java.sql.Date.class));
        registry.registerCustomEditor(java.sql.Time.class, new JSpinner.DateEditor(java.sql.Time.class));
        registry.registerCustomEditor(Timestamp.class, new JSpinner.DateEditor(
                Timestamp.class));
        return bindingResult;
    }*/
}
