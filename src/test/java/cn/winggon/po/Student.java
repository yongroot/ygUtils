package cn.winggon.po;

/**
 * Created by winggonLee on 2020/10/31
 */
public class Student extends Peo {
    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void working() {
        System.out.println("student doing");
    }
}
