package com.Route.project.modules.course.process;

import android.content.Context;
import android.content.res.Resources;
import com.xuexiang.templateproject.R;
import com.Route.project.modules.course.databean.Course;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InputCourseData {
    /*
    *ʵ���ļ����ݵ����룬����List��ʽ�洢
    *����Ϊ��λ���д洢
    *
    */
    private Context context;
    public InputCourseData(Context current){
        this.context=current;
    }
    public List<Course> ReadFile() {
        List<Course> courseDatas = new ArrayList<Course>();
        try {
            Resources r = context.getResources();
            InputStream is = r.openRawResource(R.raw.infos);
            InputStreamReader read = new InputStreamReader(is, "UTF-8");
            //InputStreamReader���Խ�һ���ֽ�����������װ���ַ�������
            BufferedReader bufferedReader = new BufferedReader(read);
            //(read);//BufferedReader�ڶ�ȡ�ı��ļ�ʱ�����Ⱦ������ļ��ж����ַ����ݲ����뻺��������֮����ʹ��read()���������ȴӻ������н��ж�ȡ��
            String lineStr = null;
            while ((lineStr = bufferedReader.readLine()) != null) {
                Course course=new Course(lineStr);
                courseDatas.add(course);
                //System.out.println(lineStr);
            }
            read.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseDatas;
    }

}
