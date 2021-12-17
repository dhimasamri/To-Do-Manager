package com.amri.todomanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    private FloatingActionButton notifyButton;
    private DataBaseHelper mainDataBase;
    private List<ToDoModel> aList;
    private ToDoAdapter doAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aRecyclerview = findViewById(R.id.recyclerview);
        floatingActionButton = findViewById(R.id.fab);
        notifyButton = findViewById(R.id.notify_button); //Notify
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



        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String GROUP_KEY_EMAILS = "group_key_emails";

                String message = "";
                String message2 = "";
                String message3 = "";

                int position = 0;

                List<ToDoModel> blist;
                blist = mainDataBase.getAllTasks();
                Collections.reverse(blist);
                doAdapter.setTasks(blist);
                for(int i = 0; i < blist.size(); i++) {
                    if(blist.get(i).getStatus() == 0) {
                        if(message == "") {
                            message = blist.get(i).getTask();
                            position = i;
                        } else if(message2 == "") {
                            message2 = blist.get(i).getTask();
                            position = i;
                        } else if(message3 == "") {
                            message3 = blist.get(i).getTask();
                            position = i;
                        }

                    }
                }

                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_baseline_library_add_check_24);


                Notification summaryNotification = new NotificationCompat.Builder(MainActivity.this, "My Notification")
                        .setContentTitle("Task To Do")
                        .setSmallIcon(R.drawable.ic_baseline_message_24)
                        .setLargeIcon(largeIcon)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine(message)
                                .addLine(message2)
                                .addLine(message3)
                                .setBigContentTitle("Your Task To Do")
                                .setSummaryText(""))
                        .setGroup(GROUP_KEY_EMAILS)
                        .setGroupSummary(true)
                        .build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(3, summaryNotification);


                /**
                //Notification code goes here

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
                // Set title
                builder.setContentTitle("This title");
                // Set content
                builder.setContentText(message);
                // Set icon
                builder.setSmallIcon(R.drawable.ic_baseline_message_24);
                // Set auto cancel
                builder.setAutoCancel(true);
                // Set Group
                builder.setGroup(GROUP_KEY_EMAILS);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                managerCompat.notify(1, builder.build());

                // Notification 2

                NotificationCompat.Builder builder2 = new NotificationCompat.Builder(MainActivity.this, "My Notification");
                // Set title
                builder2.setContentTitle("This title");
                // Set content
                builder2.setContentText(message);
                // Set icon
                builder2.setSmallIcon(R.drawable.ic_baseline_message_24);
                // Set auto cancel
                builder2.setAutoCancel(true);
                // Set Group
                builder.setGroup(GROUP_KEY_EMAILS);

                managerCompat.notify(2, builder2.build());
                **/
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