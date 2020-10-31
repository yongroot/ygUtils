package cn.winggon.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by winggonLee on 2020/10/31
 */
public class Teacher extends Peo{
    List<Student> like = new ArrayList<>();
    Map<String, List<Student>> stuNameGroup = new HashMap<>();

    public Teacher() {
    }

    public Teacher(String name) {
        this.name = name;
    }

    public List<Student> getLike() {
        return like;
    }

    public void setLike(List<Student> like) {
        this.like = like;
    }

    public Map<String, List<Student>> getStuNameGroup() {
        return stuNameGroup;
    }

    public void setStuNameGroup(Map<String, List<Student>> stuNameGroup) {
        this.stuNameGroup = stuNameGroup;
    }

    @Override
    public void working() {
        System.out.println("teacher doing");
    }
}
