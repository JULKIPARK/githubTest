package com.pwc.pwcesg.config.web;

import java.util.HashMap;

import org.springframework.jdbc.support.JdbcUtils;

@SuppressWarnings("rawtypes")
public class LowerKeyMap extends HashMap {
    private static final long serialVersionUID = -2646044785538215570L;

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String) key), value);
    }
}
