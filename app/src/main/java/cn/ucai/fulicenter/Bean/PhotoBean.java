package cn.ucai.fulicenter.Bean;

/**
 * Created by Administrator on 2016/9/7.
 */
public class PhotoBean {
    private int id;
    private String displayName;
    private String path;
    private int size;
    private int height;
    private int width;

    public PhotoBean(int id,String displayName,String path,int size,int height,int width){
        this.id=id;
        this.displayName=displayName;
        this.path=path;
        this.size=size;
        this.height=height;
        this.width=width;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "PhotoBean{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
