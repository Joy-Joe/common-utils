package cn.sy.convert;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author joy joe
 * @date 2021/12/4 上午10:30
 * @DESC
 */
public class XmlXStreamUtil {
    private static final Map<String, XStream> xStream = new ConcurrentHashMap();

    /**
     * 初始化
     *
     * @return
     * @paramobjName
     */
    private static XStream getXStream(Class<?> objName, Class[] forbiddenClasses) {
        String key = objName.getName();
        if (xStream.get(key) == null) {
            xStream.put(key, new XStream() {
                //使得xml中比JavaBean多出的字段解析不会抛异常
                @Override
                protected MapperWrapper wrapMapper(MapperWrapper next) {
                    return new MapperWrapper(next) {
                        @Override
                        public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                            if (definedIn == Object.class) {
                                try {
                                    return this.realClass(fieldName) != null;
                                } catch (Exception e) {
                                    return false;
                                }
                            } else {
                                return super.shouldSerializeMember(definedIn, fieldName);
                            }
                        }
                    };
                }
            });

            if (forbiddenClasses != null && forbiddenClasses.length > 0) {
                XStream.setupDefaultSecurity(xStream.get(key));
                xStream.get(key).allowTypes(forbiddenClasses);
            }
        }
        return xStream.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert2Bean(String resultStr, Class<T> clazz, Class[] forbiddenClasses, Converter converter, String... duplicateFields) throws Exception {
        XStream xstream = getXStream(clazz, forbiddenClasses);

        if (converter != null) {
            xstream.registerConverter(converter);
        }
        if (duplicateFields != null && duplicateFields.length > 0) {
            //出现多个重复属性，匹配为集合类型，需要JavaBean属性字段声明为集合字段类型
            for (String duplicateField : duplicateFields) {
                xstream.addImplicitCollection(clazz, duplicateField, String.class);
            }
        }
        //使注解生效
        xstream.processAnnotations(clazz);
        T obj = (T) xstream.fromXML(resultStr);
        return obj;
    }

    public static <T> String toXml(Object obj, Class<T> clazz, Converter converter) throws Exception {
        XStream xstream = new XStream();
        if (converter != null) {
            xstream.registerConverter(converter);
        }
        //使注解生效
        xstream.processAnnotations(clazz);
        return xstream.toXML(obj);
    }

    public static void toXmlFile(Object obj, Class[] clazz, Converter converter, Writer out) throws Exception {
        XStream xstream = new XStream();
        //使注解生效
        xstream.processAnnotations(clazz);
        if (converter != null) {
            xstream.registerConverter(converter);
        }
        xstream.marshal(obj, new CompactWriter(out));
    }

    public static void toFormatXmlFile(Object obj, Class[] clazz, Converter converter, Writer out) throws Exception {
        //XmlFriendlyReplacer 解决_转为__的问题
        XStream xstream = new XStream(new DomDriver(null));
        //使注解生效
        xstream.processAnnotations(clazz);
        if (converter != null) {
            xstream.registerConverter(converter);
        }
        xstream.toXML(obj, out);
    }
}
