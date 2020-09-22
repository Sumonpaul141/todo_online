package com.belivit.todoonline.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.belivit.todoonline.Interface.TodoCheckEvent;
import com.belivit.todoonline.Models.Todo;
import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.ToastUtils;

import org.w3c.dom.Text;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    List<Todo> todoList;
    Context context;
    TodoCheckEvent todoCheckEvent;

    public TodoAdapter(List<Todo> todoList, Context context, TodoCheckEvent todoCheckEvent) {
        this.todoList = todoList;
        this.context = context;
        this.todoCheckEvent = todoCheckEvent;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final TodoAdapter.ViewHolder holder, final int position) {

        final Todo todo = todoList.get(position);
        holder.todoTitleTv.setText(todo.getTodoTitle());
        holder.todoChecked.setChecked(todo.getIsDone());
        if (todo.getIsDone()){
            holder.todoTitleTv.setTextColor(Color.parseColor("#CCEEEE"));
        }else {
            holder.todoTitleTv.setTextColor(Color.parseColor("#117999"));
        }
        holder.todoChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    holder.todoTitleTv.setTextColor(Color.parseColor("#CCEEEE"));
                }else {
                    holder.todoTitleTv.setTextColor(Color.parseColor("#117999"));
                }
                todoCheckEvent.onTodoChecked(isChecked, todo, position);
            }
        });

        holder.itemDeleteIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoCheckEvent.onTodoDelete(todo.getTodoId(),position);
            }
        });
    }

    public void delete(int position){
        int newPosition = position;
        todoList.remove(newPosition);
        notifyItemRemoved(newPosition);
        notifyItemRangeChanged(newPosition, todoList.size());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoTitleTv;
        CheckBox todoChecked;
        ImageButton itemDeleteIb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoTitleTv = itemView.findViewById(R.id.itemTodoTitleTv);
            todoChecked = itemView.findViewById(R.id.todoChecked);
            itemDeleteIb = itemView.findViewById(R.id.itemDeleteIb);
        }
    }

}
