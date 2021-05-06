package com.Route.project.modules.course.UI;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.SimpleAdapter;
import com.xuexiang.templateproject.R;
import com.Route.project.modules.course.databean.Course;
import com.Route.project.modules.course.process.InputCourseData;
import com.Route.project.modules.course.process.SimpleFreeTable;

public class CourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private List<Course> Coursetable;
    private String week;
    // 数据封装为一个数组
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursetable);

        gview = (GridView) findViewById(R.id.gridView);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"长按进入上课路线导航!",Toast.LENGTH_SHORT).show();
            }
        });
        gview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //设置事件监听(长按)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"即将为您导航!",Toast.LENGTH_SHORT).show();
                //Toast.m
                return true;
            }
        });


        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        InputCourseData icd=new InputCourseData(this);
        List<Course> courseDatas=icd.ReadFile();

        SimpleFreeTable sift=new SimpleFreeTable();
        sift.FormatTheCourseData(courseDatas);
        Coursetable=sift.OutputCourseTable(courseDatas);
        week="13";

        getData(Coursetable,week);
        //新建适配器
        String [] from ={"text"};
        int [] to = {R.id.texts};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.course_items, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);

    }

    public List<Map<String, Object>> getData(List<Course> Coursetable,String  week){
        //尝试批量处理
        int day,time=0;
        String coursename = null,location;
        //构建35个map,从而进行编辑
        for(int i=0;i<35;i++){
            Map<String, Object> map = new HashMap<String, Object>();//偏移问题day+time*7
            for(Course course:Coursetable)
            {
                if(course.getWeeks().equals(week)){
                    day=Integer.parseInt(course.getWeekday());
                    time=ChangeCoursesegmentToTime(course.getTime());
                    if(i==day+time*7)
                    {
                        coursename=course.getCourseName();
                        location='@'+course.getClassRoom();
                        map.put("text", coursename+"\n"+location);
                        data_list.add(map);
                        break;
                    }
                }
            }
            if(map.isEmpty()){
                map.put("text","null");
                data_list.add(map);
            }
                //InputDataToTheCourseTableAndDisplay(day,time,coursename,location);
        }
        return data_list;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    public int ChangeCoursesegmentToTime(String Coursesegment){
        int time=0;
        switch (Coursesegment) {
            case "[01-02节]":
                time = 0;
                break;
            case "[03-04节]":
                time = 1;
                break;
            case "[05-06节]":
                time = 2;
                break;
            case "[07-08节]":
                time = 3;
                break;
            case "[09-10节]":
                time = 4;
                break;
            default:
                break;
        }
        return time;
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}