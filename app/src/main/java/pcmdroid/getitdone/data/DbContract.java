package pcmdroid.getitdone.data;

import android.provider.BaseColumns;

/**
 * Created by parth.mehta on 10/10/15.
 */
public final class DbContract {

    public DbContract() {

    }

    public static abstract class TodoItemTable implements BaseColumns {
        public static final String TABLE_NAME = "todolist";
        public static final String COLUMN_NAME_TODOITEM = "todoitem";
    }
}
