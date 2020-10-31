package cn.winggon;

import cn.winggon.po.Student;
import cn.winggon.po.Teacher;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对象工具测试用例
 * <p>
 * Created by winggonLee on 2020/10/31
 */
public class ObjUtilsTest {
    public static void main(String[] args) throws IOException {
        for (int j = 0; j < 100; j++) {
            List<Student> studentList = Stream.iterate(0, i -> new Random().nextInt(1000)).limit(10000).map(i -> new Student(String.valueOf(i))).collect(Collectors.toList());
            Teacher teacher = new Teacher("Mr.k");
            teacher.setLike(studentList);
            teacher.setStuNameGroup(MapUtils.objGrouping(studentList, s -> String.valueOf(s.getName().charAt(0))));

            long time0 = System.currentTimeMillis();
            Teacher teacher0 = ObjUtils.deepCopy(teacher);
            long time1 = System.currentTimeMillis();


            if (teacher.getLike().get(9999).getName() == teacher0.getLike().get(9999).getName()) {
                System.out.println(time1 - time0);
            }

            System.gc();
        }

        System.in.read();
    }
}
