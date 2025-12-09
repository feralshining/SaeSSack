package com.saessac.data;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.saessac.R;
import com.saessac.utils.SaessacUI;
import java.util.List;

/**
 * ListView에 투두 항목을 표시하는 커스텀 어댑터 클래스
 * BaseAdapter를 상속받아 데이터 바인딩과 뷰 재사용을 구현합니다.
 */
public class TodoListAdapter extends BaseAdapter {
    private Context context;
    private List<TodoItem> todoList;
    private TodoDBHelper dbHelper;

    /**
     * 생성자
     * 
     * @param context  액티비티 컨텍스트
     * @param todoList 표시할 투두 리스트
     * @param dbHelper 데이터베이스 헬퍼
     */
    public TodoListAdapter(Context context, List<TodoItem> todoList, TodoDBHelper dbHelper) {
        this.context = context;
        this.todoList = todoList;
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return todoList.get(position).getId();
    }

    /**
     * ListView의 각 아이템 뷰를 생성하고 데이터를 바인딩합니다.
     * ViewHolder 패턴을 사용하여 성능을 최적화합니다.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // 뷰 재사용을 위한 ViewHolder 패턴
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_todo, parent, false);
            holder = new ViewHolder();
            holder.checkbox = convertView.findViewById(R.id.todo_checkbox);
            holder.textView = convertView.findViewById(R.id.todo_text);
            holder.editButton = convertView.findViewById(R.id.edit_button);
            holder.deleteButton = convertView.findViewById(R.id.delete_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 현재 위치의 투두 항목 가져오기
        TodoItem currentItem = todoList.get(position);

        // 체크박스 설정
        holder.checkbox.setChecked(currentItem.isCompleted());
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentItem.setCompleted(isChecked);
            dbHelper.updateTodoCompleted(currentItem.getId(), isChecked);

            // 완료된 항목은 취소선 표시
            if (isChecked) {
                holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.textView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            } else {
                holder.textView.setPaintFlags(holder.textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.textView.setTextColor(context.getResources().getColor(android.R.color.black));
            }
        });

        // 텍스트 설정
        holder.textView.setText(currentItem.getTask());

        // 완료된 항목에 취소선 표시
        if (currentItem.isCompleted()) {
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        } else {
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textView.setTextColor(context.getResources().getColor(android.R.color.black));
        }

        // 편집 버튼 클릭 이벤트
        holder.editButton.setOnClickListener(v -> {
            showEditDialog(currentItem, position);
        });

        // 삭제 버튼 클릭 이벤트
        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmDialog(currentItem, position);
        });

        return convertView;
    }

    /**
     * 투두 항목 편집 다이얼로그를 표시합니다.
     * 
     * @param item     편집할 투두 항목
     * @param position 리스트에서의 위치
     */
    private void showEditDialog(TodoItem item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("할 일 수정");

        // 편집용 입력 필드
        final EditText input = new EditText(context);
        input.setText(item.getTask());
        input.setSelection(item.getTask().length());
        builder.setView(input);

        // 수정 버튼
        builder.setPositiveButton("수정", (dialog, which) -> {
            String newTask = input.getText().toString().trim();
            if (!newTask.isEmpty()) {
                item.setTask(newTask);
                dbHelper.updateTodoTask(item.getId(), newTask);
                notifyDataSetChanged();
                SaessacUI.showText(context, "수정되었습니다.");
            } else {
                SaessacUI.showText(context, "내용을 입력해주세요.");
            }
        });

        // 취소 버튼
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * 투두 항목 삭제 확인 다이얼로그를 표시합니다.
     * 
     * @param item     삭제할 투두 항목
     * @param position 리스트에서의 위치
     */
    private void showDeleteConfirmDialog(TodoItem item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("삭제 확인");
        builder.setMessage("이 항목을 삭제하시겠습니까?\n\n\"" + item.getTask() + "\"");

        // 삭제 버튼
        builder.setPositiveButton("삭제", (dialog, which) -> {
            dbHelper.deleteTodo(item.getId());
            todoList.remove(position);
            notifyDataSetChanged();
            SaessacUI.showText(context, "삭제되었습니다.");
        });

        // 취소 버튼
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * 데이터 리스트를 업데이트합니다.
     * 
     * @param newList 새로운 투두 리스트
     */
    public void updateData(List<TodoItem> newList) {
        this.todoList = newList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder 패턴을 위한 내부 클래스
     * 각 아이템 뷰의 위젯을 캐싱하여 성능을 향상시킵니다.
     */
    private static class ViewHolder {
        CheckBox checkbox;
        TextView textView;
        ImageButton editButton;
        ImageButton deleteButton;
    }
}
