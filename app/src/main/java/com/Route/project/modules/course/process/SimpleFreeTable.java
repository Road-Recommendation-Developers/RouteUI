package com.Route.project.modules.course.process;


import com.Route.project.modules.course.databean.Course;

import java.util.ArrayList;
import java.util.List;

public class SimpleFreeTable {
    /*
    *���м򵥵��޿α�������Ҫ�Ƕ����ݵĽ�������
    *
     */
    public SimpleFreeTable(){

    }

    public void FormatTheCourseData(List <Course> courseDatas) {

        //OutputSimpleCourseTable(courseDatas);
        List<Course> Coursetable=OutputCourseTable(courseDatas);
        List<Course> Freetable =OutputFreeTable(courseDatas);
    }

    //���ݻ��������ݡ�0-6���������ڼ��Ľ���
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
    //����γ̱�
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
    //����޿α�
    public List<Course>  OutputFreeTable(List <Course> courseDatas){
        int mon,flag=0;
        List<Course> tmp = new ArrayList<>(courseDatas);
        List<Course> Freetable = new ArrayList<>();
        for(int i=0;i<22;i++){
            System.out.println("No."+(i+1)+"week��");
            for(int j=0;j<courseDatas.size();j++){
                //��ʼ��������ܣ�
                //����ѧ���޿ε����
                if(courseDatas.get(j).getCourseName().equals("free")){
                    FormulateTheOutputOfFreeTable(courseDatas.get(j).getWeekday(),"free");
                    Course course= (Course) tmp.get(j).clone();
                    course.setWeeks(String.valueOf(i+1));

                    //���ھ���ʱ��Ļ�ȡ����
                    //1,ȷ����ǰʱ��ζ�Ӧ����һ��iֵ
                    //2,ִ�еݹ�
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
                    //ͬһʱ��β�ͬ�ܴ��п�,flagֵ��������
                    //�ų���ʼ������
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
                    //�ų��յ�����
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

                    //ֻ��һ�ſε�������������ſν������ɵĵ�һ�ڿΣ�����޿�
                    if (flag==0){
                        FormulateTheOutputOfFreeTable(courseDatas.get(j).getWeekday(),"free");
                        Course course= (Course) tmp.get(j).clone();
                        course.setWeeks(String.valueOf(i+1));
                        Freetable.add(course);
                    }
                    //���ſΣ��������ɵĵ�һ�ڿν������
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
    //��һ���ĸ�ʽ�������
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

    //���Ĵ��룺�����п�û�ε������
    public String AnalyseTheStatus(List <Course> courseDatas,int i,int j){
        String status="free";
        int start,end;
        //��ʼ��������ܣ�
        //����ѧ���޿ε����
        if(courseDatas.get(j).getCourseName().equals("free")){
            return status;
        }
        String[] tmp,weeks;
        tmp=courseDatas.get(j).getWeeks().split("\\(");
        //ֻ�е����пε����
        if(tmp[0].length()<=2){
            start=end=Integer.valueOf(tmp[0]).shortValue();
            if(i+1>=start&&i+1<=end){
                status="busy";
            }
        }
        //���������пε����
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
