package com.Route.project.modules.todolist.UI;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xuexiang.templateproject.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TodoList extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    private ListView listView;
    List<Map<String, Object>> arrayList=new ArrayList<Map<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"长按进入待办事务路线导航!",Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //设置事件监听(长按)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"即将为您导航!",Toast.LENGTH_SHORT).show();
                //Toast.m
                return true;
            }
        });
        floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> toAdd());
        arrayList=getData();
        String[] from = {"title", "text"};
        int[] to= new int[]{R.id.todo_item_title, R.id.todo_item_text};
        //创建适配器
        /**
         * @param context The current context. 当前的上下文
         * @param resource The resource ID for a layout file containing a TextView to use when
         *                 instantiating views.   listView子项布局的id
         * @param objects The objects to represent in the ListView.  适配的数据
         */
        ListAdapter listAdapter = new SimpleAdapter(this, arrayList, R.layout.todo_items,from, to);

        //配置适配器
        listView.setAdapter(listAdapter);
    }

    private void toAdd() {

        Intent intent=new Intent(this,AddTodoActivity.class);

        startActivity(intent);
    }


    public List<Map<String, Object>> getData(){
        for(int i=0;i<10;i++){
            Map<String,Object>value =new HashMap<String,Object>();
            value.put("title","title");
            value.put("text","text");
            arrayList.add(value);
        }
        return arrayList;

            //InputDataToTheCourseTableAndDisplay(day,time,coursename,location);
}
}