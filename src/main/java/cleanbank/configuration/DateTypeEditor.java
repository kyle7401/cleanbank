package cleanbank.configuration;

import java.beans.PropertyEditorSupport;

/**
 * Created by hyoseop on 2015-11-10.
 *
 * http://jyukutyo.hatenablog.com/entry/20100512/1273742460
 *
 */
public class DateTypeEditor extends PropertyEditorSupport {
    /*@Override
    public void setAsText(String text) throws IllegalArgumentException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
//            date = format.parse(text);
//            http://stackoverflow.com/questions/15668329/convert-string-date-to-java-sql-date
            Date parsed = (Date) format.parse(text);
        } catch (ParseException e) {
            // nop
        }
        setValue(date);
    }*/
}
