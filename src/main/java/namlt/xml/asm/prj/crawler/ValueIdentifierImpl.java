package namlt.xml.asm.prj.crawler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import namlt.xml.asm.prj.crawler.model.ValueProperties;

/**
 *
 * @author ADMIN
 */
public class ValueIdentifierImpl implements ValueIdentifier {

    private final Map<String, String> values = new HashMap<>();
    private final List<Enum> enumeration;

    public ValueIdentifierImpl(Class<? extends Enum> enumeration) {
        this.enumeration = Arrays.asList(enumeration.getEnumConstants());
    }

    @Override
    public void indentify(String key, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        enumeration.parallelStream().map(e -> {
            String[] keys = ((ValueProperties) e).getVerifyKey();
            boolean isFound = false;
            for (String key1 : keys) {
                isFound = key1.equalsIgnoreCase(key);
                if (isFound) {
                    values.put(e.name(), value);
                    break;
                }
            }
            return isFound;
        }).filter(b -> b).findFirst();
    }

    @Override
    public Map values() {
        return values;
    }

}
