package pcmdroid.getitdone;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = TodoListActivity.class.getSimpleName();
    private static final String FILE_NAME = "todo.txt";
    static final int EDIT_ITEM_REQUEST = 1;
    static final String LIST_ITEM = "list_item";
    private static final String ITEM_DELETED = "Item Deleted";

    ArrayList<String> todoItems;
    ArrayAdapter<String> todoItemsAdapter;
    ListView lvItems;
    EditText editText;
    int listItemInEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button addItemButton = (Button)findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(this);
        editText = (EditText)findViewById(R.id.newItemEditText);
        editText.clearFocus();

        readItems();

        todoItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,todoItems);
        lvItems = (ListView)findViewById(R.id.listView);
        lvItems.setAdapter(todoItemsAdapter);
        setUpListViewListeners();

    }

    private void setUpListViewListeners() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteItem(position);
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TodoListActivity.this,EditItemActivity.class);
                intent.putExtra(LIST_ITEM,todoItems.get(position));
                listItemInEdit = position;
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
            }
        });
    }

    private void deleteItem(int position) {
        todoItems.remove(position);
        Log.d(TAG, "Deleting Item number " + position + " List size:" + todoItems.size());
        todoItemsAdapter.notifyDataSetChanged();
        writeItems();
        Toast.makeText(this, ITEM_DELETED, Toast.LENGTH_SHORT).show();
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
        if(view.getId() == R.id.addItemButton) {
            String newItem = editText.getText().toString();
            todoItems.add(newItem);
            todoItemsAdapter.notifyDataSetChanged();
            writeItems();
            editText.setText("");
            hideSoftKeyboard();
        }

    }
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,FILE_NAME);
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            Log.e(TAG,"Could not open file",e);
            todoItems = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,FILE_NAME);
        try {
            FileUtils.writeLines(todoFile,todoItems);
        } catch (IOException e) {
            Log.e(TAG,"",e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                String listItem = data.getStringExtra(LIST_ITEM);
                if(listItem == null) {
                    Log.e(TAG, "List item data is null");
                }
                todoItems.set(listItemInEdit,listItem);
                todoItemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }
}
