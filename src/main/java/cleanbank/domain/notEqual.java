package cleanbank.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.kaczmarzyk.spring.data.jpa.domain.PathSpecification;
import net.kaczmarzyk.spring.data.jpa.utils.Converter;

/**
 * Created by hyoseop on 2015-11-20.
 *
 *  net.kaczmarzyk.spring.data.jpa.domain.Equal 만 존재하고 notEqual을 처리할 방법을 몰라서 만듬
 */
public class notEqual<T> extends PathSpecification<T> {
    private String expectedValue;
    private Converter converter;

    public notEqual(String path, String[] httpParamValues) {
        this(path, httpParamValues, null);
    }

    public notEqual(String path, String[] httpParamValues, String[] config) {
        super(path);
        if (httpParamValues == null || httpParamValues.length != 1) {
            throw new IllegalArgumentException();
        }
        if (config != null && config.length != 1) {
            throw new IllegalArgumentException("config may contain only one value (date format), but was: " + config);
        }
        String dateFormat = config != null ? config[0] : null;
        expectedValue = httpParamValues[0];
        converter = Converter.withDateFormat(dateFormat);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Class<?> typeOnPath = path(root).getJavaType();
//        return cb.equal(path(root), converter.convert(expectedValue, typeOnPath));
        return cb.notEqual(path(root), converter.convert(expectedValue, typeOnPath));
    }
}
