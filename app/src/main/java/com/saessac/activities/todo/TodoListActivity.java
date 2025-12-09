package com.saessac.activities.todo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.saessac.R;
import com.saessac.utils.SaessacUI;
import com.saessac.utils.SaessacUserData;
import com.saessac.data.TodoDBHelper;
import com.saessac.data.TodoItem;
import com.saessac.data.TodoListAdapter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.saessac.activities.main.MainActivity;

/**
 * 투두리스트 작성 및 관리 화면 - ListView와 SQLite DB를 활용한 리팩토링 버전
 * 기말 과제용으로 개선된 코드입니다.
 * 
 * 주요 기능:
 * - ListView를 통한 투두 항목 표시
 * - SQLite DB를 활용한 데이터 저장 및 조회
 * - CRUD (Create, Read, Update, Delete) 기능
 * - 검색 기능
 * - 완료 상태 관리
 */
public class TodoListActivity extends AppCompatActivity {
    // UI 컴포넌트
    private ListView todoListView;
    private EditText inputText;
    private EditText searchInput;
    private Button searchBtn;
    private Button showAllBtn;

    // 데이터 관리
    private TodoDBHelper dbHelper;
    private TodoListAdapter adapter;
    private List<TodoItem> todoList;
    private String currentDate;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_todolist_writing_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_todolist_writing_theme_1);
                break;
        }

        // 현재 날짜 저장
        currentDate = SaessacUI.date;

        // UI 초기화
        initializeViews();

        // DB 헬퍼 초기화
        dbHelper = new TodoDBHelper(this);

        // 투두리스트 로드
        loadTodoList();

        // 브금 설정 값 로드
        JsonObject jsonData = SaessacUserData.loadJSON(getBaseContext().getFilesDir().toString() + "/" + "Config.json");
        if (jsonData != null && jsonData.has("bgm_toggle")) {
            boolean isBGM = jsonData.get("bgm_toggle").getAsBoolean();
            if (isBGM) {
                SaessacUI.playMusic(this);
            } else {
                SaessacUI.stopMusic();
            }
        }

        // [ 버튼 - 뒤로 가기 ]
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // [ 버튼 - 항목 추가 (DB에 저장) ]
        ImageButton addBTN = findViewById(R.id.add_btn);
        addBTN.setOnClickListener(v -> {
            String task = inputText.getText().toString().trim();
            if (task.isEmpty()) {
                SaessacUI.showText(this, "내용을 입력해주세요.");
                return;
            }

            // DB에 추가
            long result = dbHelper.addTodo(currentDate, task);
            if (result != -1) {
                inputText.setText("");
                loadTodoList();
                SaessacUI.showText(this, "추가되었습니다.");
            } else {
                SaessacUI.showText(this, "추가 실패. 다시 시도해주세요.");
            }
        });

        // [ 버튼 - 완료된 항목 일괄 삭제 ]
        ImageButton deleteBTN = findViewById(R.id.delete_btn);
        deleteBTN.setOnClickListener(v -> {
            // 완료된 항목만 삭제
            int deletedCount = 0;
            for (int i = todoList.size() - 1; i >= 0; i--) {
                TodoItem item = todoList.get(i);
                if (item.isCompleted()) {
                    dbHelper.deleteTodo(item.getId());
                    deletedCount++;
                }
            }

            if (deletedCount > 0) {
                loadTodoList();
                SaessacUI.showText(this, deletedCount + "개 항목이 삭제되었습니다.");
            } else {
                SaessacUI.showText(this, "완료된 항목이 없습니다.");
            }
        });

        // 검색 버튼
        searchBtn.setOnClickListener(v -> {
            String keyword = searchInput.getText().toString().trim();
            if (keyword.isEmpty()) {
                SaessacUI.showText(this, "검색어를 입력해주세요.");
                return;
            }
            searchTodos(keyword);
        });

        // 전체 보기 버튼
        showAllBtn.setOnClickListener(v -> {
            searchInput.setText("");
            loadTodoList();
        });
    }

    /**
     * UI 컴포넌트를 초기화합니다.
     */
    private void initializeViews() {
        todoListView = findViewById(R.id.todo_listview);
        inputText = findViewById(R.id.input_txt);
        searchInput = findViewById(R.id.search_input);
        searchBtn = findViewById(R.id.search_btn);
        showAllBtn = findViewById(R.id.show_all_btn);
    }

    /**
     * DB에서 현재 날짜의 투두 리스트를 로드합니다.
     */
    private void loadTodoList() {
        todoList = dbHelper.getTodosByDate(currentDate);

        if (adapter == null) {
            adapter = new TodoListAdapter(this, todoList, dbHelper);
            todoListView.setAdapter(adapter);
        } else {
            adapter.updateData(todoList);
        }

        // 항목 개수 표시
        int totalCount = todoList.size();
        int completedCount = 0;
        for (TodoItem item : todoList) {
            if (item.isCompleted()) {
                completedCount++;
            }
        }

        if (totalCount > 0) {
            setTitle("투두리스트 (" + completedCount + "/" + totalCount + " 완료)");
        } else {
            setTitle("투두리스트");
        }
    }

    /**
     * 키워드로 투두 항목을 검색합니다.
     * 
     * @param keyword 검색 키워드
     */
    private void searchTodos(String keyword) {
        List<TodoItem> searchResults = dbHelper.searchTodos(keyword);

        if (searchResults.isEmpty()) {
            SaessacUI.showText(this, "검색 결과가 없습니다.");
        } else {
            adapter.updateData(searchResults);
            SaessacUI.showText(this, searchResults.size() + "개 항목을 찾았습니다.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaessacUI.stopMusic();
    }
}
