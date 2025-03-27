package main.java.model;

import java.util.List;
import java.util.Objects;

public class ScrapingResult {
    
    private boolean sucess;
    private String message;
    private List<Anexo> downAttachments;
    private String pathZip;
    private long executionTime; //ms

    public ScrapingResult(boolean sucess, String message, List<Anexo> downAttachments, String pathZip, long executionTime){
        this.sucess = sucess;
        this.message = Objects.requireNonNull(message, "Message cannot be null");
        this.downAttachments = Objects.requireNonNull(downAttachments, "Attachments list cannot be null");
        this.pathZip = pathZip;
        this.executionTime = executionTime;
    }

    //Construct_2 --> Caso de falha
    public ScrapingResult(boolean sucess, String message){
        this(sucess, message, List.of(), null, 0);
    }

    //getters
    public boolean isSucess(){
        return this.sucess;
    }

    public String getMessage(){
        return this.message;
    }

    public List<Anexo> getDownAttachments(){
        return this.downAttachments;
    }

    public String getPathZip(){
        return this.pathZip;
    }

    public long getExecutionTime(){
        return this.executionTime;
    }

    //setters
    public void setSucess(boolean sucess){
        this.sucess = sucess;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setDownAttachments(List<Anexo> downAttachments){
        this.downAttachments = downAttachments;
    }

    public void setPathZip(String pathZip){
        this.pathZip = pathZip;
    }

    public void setExecutionTime(){
        this.executionTime = executionTime;
    }

    //Métodos
    public int getDowloaddedCount(){
        return downAttachments.size();
    }

    @Override
    public String toString() {
        return "ScrapingResult{" +
               "sucess=" + sucess +
               ", message='" + message + '\'' +
               ", downloadedAttachments=" + downAttachments.size() +
               ", pathZip='" + pathZip + '\'' +
               ", executionTime=" + executionTime +
               '}';
    }
}
