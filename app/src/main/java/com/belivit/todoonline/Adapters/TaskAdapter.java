package com.belivit.todoonline.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.belivit.todoonline.Models.Task;
import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.ToastUtils;
import com.belivit.todoonline.Views.DetailsTaskActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> taskList;
    private Context context;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.ViewHolder holder, int position) {
        final Task task = taskList.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescreiption());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsTaskActivity.class);
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescreiption());
                intent.putExtra("taskId", task.getTaskId());
                context.startActivity(intent);
            }
        });

        if (task.isArchive()){
            holder.itemUnArchivedIV.setVisibility(View.VISIBLE);
        }else {
            holder.itemUnArchivedIV.setVisibility(View.GONE);
        }

        holder.itemUnArchivedIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastOk(context, "UnArchived will be called");
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView itemUnArchivedIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitleTV);
            description = itemView.findViewById(R.id.itemDescriptionTV);
            itemUnArchivedIV = itemView.findViewById(R.id.itemUnArchivedIV);
        }
    }
}
