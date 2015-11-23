package pcmdroid.getitdone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by parth.mehta on 11/23/15.
 */
public class TodoListDataSource {
        private static final String TAG = TodoListDataSource.class.getSimpleName();

        private static String TodoLIstColumns[] = {
                DbContract.TodoItemTable._ID,
                DbContract.TodoItemTable.COLUMN_NAME_TODOITEM
        };

        private static final String SELECT_ALL_QUERY = "SELECT * FROM " + DbContract.TodoItemTable.TABLE_NAME;

        public static TodoItem insertNewTodoItem(String todoItem, Context context) {
            AppDbHelper dbHelper = new AppDbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(DbContract.TodoItemTable.COLUMN_NAME_TODOITEM, todoItem);

            long newRowId;
            newRowId = db.insertWithOnConflict (
                    DbContract.TodoItemTable.TABLE_NAME,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);

            return new TodoItem(newRowId,todoItem);
        }

        public static void updateTodoItem(TodoItem todoItem,Context context) {
            AppDbHelper dbHelper = new AppDbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(DbContract.TodoItemTable._ID, todoItem.getId());
            values.put(DbContract.TodoItemTable.COLUMN_NAME_TODOITEM, todoItem.getTodoItem());

            long newRowId;
            newRowId = db.insertWithOnConflict (
                    DbContract.TodoItemTable.TABLE_NAME,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);

        }

        public static TodoItem getTodoItem(int position, Context context) {
            Cursor cursor = queryAllTodoItemsGetCursor(context);
            cursor.move(position);
            TodoItem todoItem = cursorToTodoItem(cursor);
            cursor.close();
            return todoItem;
        }

        public static void deleteTodoItem(long id, Context context) {
            AppDbHelper dbHelper = new AppDbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            final String whereClause = DbContract.TodoItemTable._ID + "=?";
            String[] whereArgs = new String[] { String.valueOf(id) };
            db.delete(DbContract.TodoItemTable.TABLE_NAME, whereClause, whereArgs);
        }

        public static Cursor queryAllTodoItemsGetCursor(Context context) {
            AppDbHelper dbHelper = new AppDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DbContract.TodoItemTable.TABLE_NAME, TodoLIstColumns, null, null, null, null, DbContract.TodoItemTable._ID + " DESC");
            cursor.moveToFirst();
            return cursor;
        }



        public static TodoItem cursorToTodoItem(Cursor cursor) {
            TodoItem item = new TodoItem();
            item.set_id(cursor.getLong(0));
            item.setTodoItem(cursor.getString(1));
            return item;
        }
}
