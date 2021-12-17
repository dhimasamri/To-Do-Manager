package com.amri.todomanager.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amri.todomanager.MainActivity;
import com.amri.todomanager.Model.ToDoModel;
import com.amri.todomanager.R;
import com.amri.todomanager.AddNewTask;
import com.amri.todomanager.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> bList;
    private MainActivity mainActivity;
    private DataBaseHelper adapterDataBase;

    public ToDoAdapter(DataBaseHelper dataBase, MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.adapterDataBase = dataBase;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = bList.get(position);
        holder.aCheckBox.setText(item.getTask());
        holder.aCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.aCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    adapterDataBase.updateStatus(item.getId(), 1);
                }else
                    adapterDataBase.updateStatus(item.getId(), 0);
            }
        });
    }

    public boolean toBoolean(int num){

        return num!=0;
    }

    public Context getContext(){

        return mainActivity;
    }

    public void setTasks(List<ToDoModel> aList){
        this.bList = aList;
        notifyDataSetChanged();
    }

    public void deletTask(int position){
        ToDoModel item = bList.get(position);
        adapterDataBase.deleteTask(item.getId());
        bList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = bList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(mainActivity.getSupportFragmentManager(), task.getTag());


    }

    @Override
    public int getItemCount() {
        return bList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox aCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            aCheckBox = itemView.findViewById(R.id.layoutcheckbox);
        }
    }
}
