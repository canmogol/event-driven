package com.lambstat.module.jetty.data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class AbstractResponse implements Response {

    private Status status;
    private List<Param<String, Object>> params;
    private String type;

    public AbstractResponse() {
        params = new ArrayList<>();
        this.status = Status.STATUS_OK;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Param<String, Object>> getParams() {
        return params;
    }

    public void setParams(List<Param<String, Object>> params) {
        this.params = params;
    }

    @Override
    public String getType() {
        if(type == null){
            type = getClass().getSimpleName();
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
