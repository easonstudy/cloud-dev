package com.cjcx.member.utils;

import com.cjcx.member.framework.web.MyDateFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class JacksonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    private static MyDateFormat df = new MyDateFormat(mapper.getDateFormat());

    /*public static JacksonUtil1 getInstance() {
        return mapper;
    }*/

    public JacksonUtil() {
        //DateFormat dateFormat = mapper.getDateFormat();
        //mapper.setDateFormat(new MyDateFormat(dateFormat));
    }

    public static String bean2Json(Object obj) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
        mapper.writeValue(gen, obj);
        gen.close();
        return sw.toString();
    }

    public static <T> T json2Bean(String jsonStr, Class<T> objClass)
            throws JsonParseException, JsonMappingException, IOException {
        //mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        /**
         * json 转实体  允许未知属性(字段)
         * 如json: {id:1, name:'aa', age: 12};
         *   实体: id,name 共2个属性
         */
        mapper.setDateFormat(df);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonStr, objClass);
    }
}
