package com.tii.php.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhpUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(PhpUtil.class);
	 /**
     * The string representation of a Json object will be returned to the knowledge base for templated responses.  Escape all funky chars so
     * client can Json.parse it on their side.
     * @param data
     * @return
     */
    public static String safeJsonValueString(String data) {
        return data.replace("\"", "\\\"");
    }

    /**
     * Map and translate POJO to Json string
     * @param object
     * @return String representation of Json object
     */
    public static String objectToJsonString(Object object) {

        ObjectMapper mapper = new ObjectMapper();
        String data;
        try {
            data = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception trying to map JSON to String to return in data response details: " + e.getMessage());
            return null;
        }

        // save json translation to string
        if (data != null && !data.isEmpty()) {
            data = PhpUtil.safeJsonValueString(data);
        }

        return data;
    }
}
