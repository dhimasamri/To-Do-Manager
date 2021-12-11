package com.amri.todomanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.amri.todomanager.Adapter.ToDoAdapter;
import com.amri.todomanager.Model.ToDoModel;
import com.amri.todomanager.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListner {

    private RecyclerView aRecyclerview;
    private FloatingActionButton floatingActionButton;
    private DataBaseHelper mainDataBase;
    private List<ToDoModel> aList;
    private ToDoAdapter doAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aRecyclerview = findViewById(R.id.recyclerview);
        floatingActionButton = findViewById(R.id.fab);
        mainDataBase = new DataBaseHelper(MainActivity.this);
        aList = new ArrayList<>();
        doAdapter = new ToDoAdapter(mainDataBase, MainActivity.this);

        aRecyclerview.setHasFixedSize(true);
        aRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        aRecyclerview.setAdapter(doAdapter);

        aList = mainDataBase.getAllTasks();
        Collections.reverse(aList);
        doAdapter.setTasks(aList);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(doAdapter));
        itemTouchHelper.attachToRecyclerView(aRecyclerview);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        aList = mainDataBase.getAllTasks();
        Collections.reverse(aList);
        doAdapter.setTasks(aList);
        doAdapter.notifyDataSetChanged();
    }
}