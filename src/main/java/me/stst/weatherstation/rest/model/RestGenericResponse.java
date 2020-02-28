package me.stst.weatherstation.rest.model;

public class RestGenericResponse<T> {
    private T payload;
    private String msg;
    private Integer status;
    private Boolean successful=false;

    public RestGenericResponse(T payload,String msg){
        this.payload=payload;
        this.msg=msg;
        this.successful=true;
    }

    public RestGenericResponse(T payload){
        this(payload,null);
    }

    public RestGenericResponse(String errorMsg,Integer status){
        this.msg=errorMsg;
        this.status=status;
    }

    public RestGenericResponse(String errorMsg){
        this(errorMsg,null);
    }
    public RestGenericResponse(){

    }
    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public void setPayload(T payload) {
        this.payload = payload;
        this.successful=true;
    }

    public void setError(String msg){
        this.msg=msg;
        successful=false;
    }

    public T getPayload() {
        return payload;
    }

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccessful() {
        return successful;
    }
}
