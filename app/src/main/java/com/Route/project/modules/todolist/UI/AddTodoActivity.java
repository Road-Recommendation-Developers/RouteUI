package com.Route.project.modules.todolist.UI;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.xuexiang.templateproject.R;

public class AddTodoActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);
        imageView=findViewById(R.id.calendar);
        imageView.setOnClickListener(v -> toTime());
    }

    private void toTime() {
        Intent intent=new Intent(this,TimeActivity.class);
        startActivity(intent);
    }

    
}