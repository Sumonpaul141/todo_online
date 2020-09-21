package com.belivit.todoonline.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.belivit.todoonline.Models.Todo;
import com.belivit.todoonline.R;

import org.w3c.dom.Text;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    List<Todo> todoList;
    Context context;

    public TodoAdapter(List<Todo> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.todoTitleTv.setText(todo.getTodoTitle());
        holder.todoChecked.setChecked(todo.getIsDone());
        if (todo.getIsDone()){
            holder.itemView.setBackgroundColor(Color.parseColor("#8C999C"));
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoTitleTv;
        CheckBox todoChecked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoTitleTv = itemView.findViewById(R.id.itemTodoTitleTv);
            todoChecked = itemView.findViewById(R.id.todoChecked);
        }
    }
}
