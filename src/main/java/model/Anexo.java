package model;

public class Anexo {
    private String name;
    private String url;
    private String path;

    public Anexo (String name, String url){
        this.name = name;
        this.url = url;
    }

    //getters
    public String getName(){
        return this.name;
    }

    public String getUrl(){
        return this.url;
    }

    public String getPath(){
        return this.path;
    }

    //setters
    public void setName(String name){
        this.name = name;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setPath(String path){
        this.path = path;
    }
}
