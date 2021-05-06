package com.Route.project.modules.course.process;


import com.Route.project.modules.course.databean.Course;

import java.util.ArrayList;
import java.util.List;

public class SimpleFreeTable {
    /*
    *进行简单的无课表开发，主要是对数据的解析工作
    *
     */
    public SimpleFreeTable(){

    }

    public void FormatTheCourseData(List <Course> courseDatas) {

        //OutputSimpleCourseTable(courseDatas);
        List<Course> Coursetable=OutputCourseTable(courseDatas);
        List<Course> Freetable =OutputFreeTable(courseDatas);
    }

    //根据基本的数据“0-6”进行星期几的解析
    public String AnalyseTheWeekday(String weekday){
        String day;
        if(weekday.equals("0"))
            day="Monday:";
        else if(weekday.equals("1"))
            day="Tuesday:";
        else if(weekday.equals("2"))
            day="Wednesday:";
        else if(weekday.equals("3"))
            day="Thursday:";
        else if(weekday.equals("4"))
            day="Friday:";
        else if(weekday.equals("5"))
            day="Saturday:";
        else if(weekday.equals("6"))
            day="Sunday:";
        else
            day="Wrong:";
        return day;
    }
    //输出课程表
    public List<Course> OutputCourseTable(List <Course> courseDatas){
        List<Course> tmp = new ArrayList<>(courseDatas);
        List<Course> Coursetable = new ArrayList<>();
        for(int i=0;i<22;i++){
            System.out.println("No."+(i+1)+ " week:");
            for(int j=0;j<tmp.size();j++){
                if(tmp.get(j).getCourseName().equals("free")){
                    FormulateTheOutputOfCourseTable(tmp.get(j).getWeekday(),"free");
                    continue;
                }
                if(AnalyseTheStatus(tmp,i,j).equals("busy")){
                    Course course= (Course) tmp.get(j).clone();
                    course.setWeeks(String.valueOf(i+1));
                    Coursetable.add(course);
                    FormulateTheOutputOfCourseTable(tmp.get(j).getWeekday(), tmp.get(j).getCourseName());
                }
            }
        }
        //writeFile(Coursetable);
        return Coursetable;

    }
    //输出无课表
    public List<Course>  OutputFreeTable(List <Course> courseDatas){
        int mon,flag=0;
        List<Course> tmp = new ArrayList<>(courseDatas);
        List<Course> Freetable = new ArrayList<>();
        for(int i=0;i<22;i++){
            System.out.println("No."+(i+1)+"week：");
            for(int j=0;j<courseDatas.size();j++){
                //起始周与结束周；
                //整个学期无课的情况
                if(courseDatas.get(j).getCourseName().equals("free")){
                    FormulateTheOutputOfFreeTable(courseDatas.get(j).getWeekday(),"free");
                    Course course= (Course) tmp.get(j).clone();
                    course.setWeeks(String.valueOf(i+1));

                    //关于具体时间的获取问题
                    //1,确定当前时间段对应的周一的i值
                    //2,执行递归
                    mon=j;
                    while(mon>=0) {
                        if (courseDatas.get(mon).getWeekday().equals("0")){
                            course.setTime(AnalyzeTheTime(courseDatas, mon));
                            break;
                        }
                        mon--;
                    }
                    Freetable.add(course);
                    continue;
                }
                if(AnalyseTheStatus(courseDatas,i,j).equals("free")){
                    //同一时间段不同周次有课,flag值降序生成
                    //排除起始点的情况
                    if(j>1&&courseDatas.get(j).getWeekday().equals(courseDatas.get(j-2).getWeekday())){
                        if(AnalyseTheStatus(courseDatas,i,j-2).equals("busy")) {
                            continue;
                        }
                        else
                            flag=3;
                    }

                    if(j>0&&courseDatas.get(j).getWeekday().equals(courseDatas.get(j-1).getWeekday())){
                        if(AnalyseTheStatus(courseDatas,i,j-1).equals("busy")){
                            continue;
                        }
                        else
                            flag=2;
                    }
                    //排除终点的情况
                    if(j<courseDatas.size()-1&&courseDatas.get(j).getWeekday().equals(courseDatas.get(j+1).getWeekday())){
                        if(AnalyseTheStatus(courseDatas,i,j+1).equals("busy")) {
                            continue;
                        }
                        else
                            flag=1;
                    }
                    if(j<courseDatas.size()-2&&courseDatas.get(j).getWeekday().equals(courseDatas.get(j+2).getWeekday())){
                        if(AnalyseTheStatus(courseDatas,i,j+2).equals("busy")){
                            continue;
                        }
                        else
                            flag=0;
                    }

                    //只有一门课的情况，或者三门课降序生成的第一节课，输出无课
                    if (flag==0){
                        FormulateTheOutputOfFreeTable(courseDatas.get(j).getWeekday(),"free");
                        Course course= (Course) tmp.get(j).clone();
                        course.setWeeks(String.valueOf(i+1));
                        Freetable.add(course);
                    }
                    //两门课，降序生成的第一节课进行输出
                    else if (flag==1){
                        if(j==0||!courseDatas.get(j).getWeekday().equals(courseDatas.get(j-1).getWeekday())){
                            FormulateTheOutputOfFreeTable(courseDatas.get(j).getWeekday(),"free");
                            Course course= (Course) tmp.get(j).clone();
                            course.setWeeks(String.valueOf(i+1));
                            Freetable.add(course);
                        }
                    }
                    else
                        flag=0;

                }
            }
        }
        return Freetable;
    }
    //以一定的格式加以输出
    public void FormulateTheOutputOfCourseTable(String weekday,String status){
        if(weekday.equals("6")){
            if(status.equals("free"))
                System.out.println();
            else
                System.out.println(AnalyseTheWeekday(weekday)+status);
        }
        else {
            if(status.equals("free"))
                System.out.print("");
            else
                System.out.print(AnalyseTheWeekday(weekday)+status+"-->");

        }

    }
    public void FormulateTheOutputOfFreeTable(String weekday,String status){
        if(weekday.equals("6")){
            if(status.equals("free"))
                System.out.println(AnalyseTheWeekday(weekday)+status);
            else
                System.out.println();
        }
        else {
            if(status.equals("free"))
                System.out.print(AnalyseTheWeekday(weekday)+status+"-->");
            else
                System.out.print("");

        }

    }

    //核心代码：分析有课没课的情况。
    public String AnalyseTheStatus(List <Course> courseDatas,int i,int j){
        String status="free";
        int start,end;
        //起始周与结束周；
        //整个学期无课的情况
        if(courseDatas.get(j).getCourseName().equals("free")){
            return status;
        }
        String[] tmp,weeks;
        tmp=courseDatas.get(j).getWeeks().split("\\(");
        //只有单周有课的情况
        if(tmp[0].length()<=2){
            start=end=Integer.valueOf(tmp[0]).shortValue();
            if(i+1>=start&&i+1<=end){
                status="busy";
            }
        }
        //延续几周有课的情况
        else {
            weeks=tmp[0].split("-");
            start=Integer.valueOf(weeks[0]).shortValue();
            end=Integer.valueOf(weeks[1]).shortValue();
            if(i+1>=start&&i+1<=end){
                status="busy";
            }
        }

        return status;
    }

    public String AnalyzeTheTime(List <Course> courseDatas,int i){
        if(courseDatas.get(i).getTime()!=null)
            return courseDatas.get(i).getTime();
        else
            return AnalyzeTheTime(courseDatas,i+1);
    }


}
