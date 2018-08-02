package peperact.common.helper;

import com.google.common.base.CaseFormat;

public class StringHelper {
    public static String removePrefix(String src, String prefix) {
        if (!src.startsWith(prefix))
            throw new IllegalArgumentException(String.format("'%s' does not start with '%s'", src, prefix));
        return src.substring(prefix.length());
    }

    public static String camelCaseToSnakeCase(String input) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, input);
    }
}
