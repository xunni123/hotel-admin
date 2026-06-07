package com.xunni.hotel.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * XSS请求包装器，对请求参数进行XSS过滤
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return cleanXss(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = cleanXss(values[i]);
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        Map<String, String[]> cleanMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = cleanXss(values[i]);
            }
            cleanMap.put(entry.getKey(), values);
        }
        return cleanMap;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return cleanXss(value);
    }

    /**
     * 清理XSS攻击代码
     */
    private String cleanXss(String value) {
        if (value == null) {
            return null;
        }
        // 使用Apache Commons Lang进行HTML转义
        value = StringEscapeUtils.escapeHtml4(value);
        // 过滤常见的XSS攻击脚本
        value = value.replaceAll("<script[^>]*?>.*?</script>", "");
        value = value.replaceAll("<[^>]*>", "");
        value = value.replaceAll("javascript:", "");
        value = value.replaceAll("onload[^=]*=", "");
        value = value.replaceAll("onclick[^=]*=", "");
        value = value.replaceAll("onmouseover[^=]*=", "");
        value = value.replaceAll("onerror[^=]*=", "");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']", "\"\"");
        return value;
    }
}