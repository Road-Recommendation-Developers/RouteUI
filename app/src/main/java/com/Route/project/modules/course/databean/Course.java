package com.Route.project.modules.course.databean;

public class Course implements Cloneable{
    /*
     * @courseIndexIndex:������Ŀǰ��Ϊ����
     * week���γ̳�������
     * time�������Ͽ�ʱ��
     * courseName���γ�����
     * courseTeacher���ο���ʦ
     * place���Ͽν�ѧ¥
     * classroom���Ͽν���
     * introduction:��Ҫ����
     * day:����ĳ��
     * */
    private int courseIndex;
    private String weeks;
    private String weekday;
    private String time;
    private String courseName;
    private String courseTeacher;
    private String place;
    private String classroom;
    private String introduction;
    public Course(String lineStr){
        String str[] = lineStr.split(" ");
        //���������ݣ�ͨ�����з����в��

        if(str.length==2)
        {
            this.courseName =str[0];
            this.weekday=str[1];
        }

        else if(str.length==8) {
            this.courseName = str[0];
            this.courseTeacher = str[1];
            this.weeks = str[2];
            this.time = str[3];
            this.place = str[4];
            this.classroom = str[5];
            this.introduction = str[6];
            this.weekday = str[7];
        }

    }

    //���������ݻ�ȡ������
    public int getCourseIndex() {
        return courseIndex;
    }

    public void setCourseIndex(int courseIndex) {
        this.courseIndex= courseIndex;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {this.place= place; }

    public String getClassRoom() {
        return classroom;
    }

    public void setClassroom(String classroom) {this.classroom= classroom; }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {this.introduction= introduction; }

    public String getWeekday(){ return  weekday;}

    public void setWeekday(String weekday){this.weekday=weekday; }

    //����������ݵĿ�¡������ͨ�����ݿ���ʵ�ֲ�ͬ�Ĺ���
    @Override
    public Object clone() {
        Course course = null;
        try{
            course = (Course) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return course;
    }
}
