package pcmdroid.getitdone;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import pcmdroid.getitdone.data.DbContract;
import pcmdroid.getitdone.data.TodoItem;
import pcmdroid.getitdone.data.TodoListDataSource;

public class TodoListActivity extends AppCompatActivity implements View.OnClickListener, EditDialogFragment.OnCompleteListener {

    private static final String TAG = TodoListActivity.class.getSimpleName();
    static final int EDIT_ITEM_REQUEST = 1;
    static final String LIST_ITEM = "list_item";
    private static final String ITEM_DELETED = "Item Deleted";

    SimpleCursorAdapter todoItemsAdapter;
    ListView lvItems;
    EditText editText;
    long listItemInEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.newItemEditText);
        editText.clearFocus();

        todoItemsAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                TodoListDataSource.queryAllTodoItemsGetCursor(this),
                new String[]{DbContract.TodoItemTable.COLUMN_NAME_TODOITEM},
                new int[]{android.R.id.text1});

        lvItems = (ListView) findViewById(R.id.listView);
        lvItems.setAdapter(todoItemsAdapter);
        setUpListViewListeners();

    }

    private void setUpListViewListeners() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTodoItem(id);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem = TodoListDataSource.getTodoItem(position, TodoListActivity.this);
                listItemInEdit = todoItem.getId();
                showEditDialog(todoItem.getTodoItem());
            }
        });
    }

    private void deleteTodoItem(long id) {
        TodoListDataSource.deleteTodoItem(id, TodoListActivity.this);
        todoItemsAdapter.changeCursor(TodoListDataSource.queryAllTodoItemsGetCursor(TodoListActivity.this));
        Toast.makeText(TodoListActivity.this, ITEM_DELETED, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addItemButton) {
            String newItem = editText.getText().toString();
            TodoListDataSource.insertNewTodoItem(newItem, this);
            todoItemsAdapter.changeCursor(TodoListDataSource.queryAllTodoItemsGetCursor(this));
            editText.setText("");
            hideSoftKeyboard();
        }

    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                String listItem = data.getStringExtra(LIST_ITEM);
                if (listItem == null) {
                    Log.e(TAG, "List item data is null");
                    updateListItem(listItem);
                }

            }
        }
    }

    private void updateListItem(String listItem) {
        TodoItem todoItem = new TodoItem(listItemInEdit, listItem);
        TodoListDataSource.updateTodoItem(todoItem, this);
        todoItemsAdapter.changeCursor(TodoListDataSource.queryAllTodoItemsGetCursor(this));
    }
    private void showEditDialog(String listItem) {
        Bundle bundle = new Bundle();
        bundle.putString(EditDialogFragment.TODO_ITEM,listItem);
        DialogFragment newFragment = new EditDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onComplete(String listItem) {
        updateListItem(listItem);
    }


}
