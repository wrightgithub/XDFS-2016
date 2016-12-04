package com.xdfs.protocol;

import java.awt.*;

/**
 * Created by xyy on 16-12-4.
 */

public    class  Meta{

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List getValues() {
        return values;
    }

    public void setValues(List values) {
        this.values = values;
    }

    private String key;
    private List   values;
}
