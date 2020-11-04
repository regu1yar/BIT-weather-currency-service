package bit.wcservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlUtils {
    public static<T> T deserialize(String xml_string, Class<? extends T> clazz) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xml_string, clazz);
    }
}
