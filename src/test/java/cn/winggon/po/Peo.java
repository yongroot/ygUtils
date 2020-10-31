package cn.winggon.po;

/**
 * Created by winggonLee on 2020/10/31
 */
public abstract class Peo implements Doing{
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
