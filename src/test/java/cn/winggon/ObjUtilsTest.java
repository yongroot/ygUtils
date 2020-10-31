package cn.winggon;

import cn.winggon.po.Student;
import cn.winggon.po.Teacher;

import java.io.IOException;
import java.util.ArrayList;
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

        List<List<Student>> ssList = new ArrayList<>();

        for (int j = 0; j < 100; j++) {
            ssList.add(Stream.iterate(0, i -> new Random().nextInt(1000))
                    .limit(10000)
                    .map(i -> new Student(String.valueOf(i)))
                    .collect(Collectors.toList()));
        }

        final ObjUtilsTest demo = new ObjUtilsTest();
        for (int i = 0; i < 10; i++) {
            demo.test(ssList,false);
            demo.test(ssList,true);
            System.out.println("---");
        }
    }

    private void test(List<List<Student>> sList, boolean async){
        System.gc();
        long time0 = System.currentTimeMillis();

        AsyncFuture future = AsyncFuture.create();

        List<Teacher> tList = new ArrayList<>();
        List<Teacher> tList0 = new ArrayList<>();
        for (List<Student> studentList : sList) {
            Teacher teacher = new Teacher("Mr.k");
            teacher.setLike(studentList);
            teacher.setStuNameGroup(MapUtils.objGrouping(studentList, s -> String.valueOf(s.getName().charAt(0))));

            Teacher teacher0;
            if (async) {
                teacher0 = future.supply(() -> ObjUtils.deepClone(teacher), new Teacher());
            } else {
                teacher0 = ObjUtils.deepClone(teacher);
            }

            tList.add(teacher);
            tList0.add(teacher0);
        }

        if (async) {
            future.await();
        }

        char c = tList.get(tList.size() - 1).getLike().get(9999).getName().charAt(0);
        List<Student> students = tList.get(tList.size() - 1).getStuNameGroup().get(String.valueOf(c));
        List<Student> students0 = tList0.get(tList0.size() - 1).getStuNameGroup().get(String.valueOf(c));

        if (students.get(students.size() - 1).getName() == students0.get(students0.size() - 1).getName()) {
            System.out.println((async ? "并行耗时" : "串行耗时") + (System.currentTimeMillis() - time0));
        }

        System.gc();
    }
}
