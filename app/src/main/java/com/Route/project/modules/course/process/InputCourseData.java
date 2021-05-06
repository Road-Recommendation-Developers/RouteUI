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
    *实现文件内容的输入，并以List形式存储
    *以行为单位进行存储
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
            //InputStreamReader可以将一个字节输入流包包装成字符输入流
            BufferedReader bufferedReader = new BufferedReader(read);
            //(read);//BufferedReader在读取文本文件时，会先尽量从文件中读入字符数据并置入缓冲区，而之后若使用read()方法，会先从缓冲区中进行读取。
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
