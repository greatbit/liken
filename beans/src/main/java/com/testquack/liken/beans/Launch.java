package com.testquack.liken.beans;

import java.util.HashMap;
import java.util.Map;

public class Launch extends BaseLaunch{
    private Map<String, Object> metadata = new HashMap<>();

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
