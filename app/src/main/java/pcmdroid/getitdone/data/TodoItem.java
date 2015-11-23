package pcmdroid.getitdone.data;

/**
 * Created by parth.mehta on 11/23/15.
 */
public class TodoItem {
    private Long _id;


    private String todoItem;

    public TodoItem(long _id,String todoItem) {
        this._id = _id;
        this.todoItem = todoItem;
    }

    public TodoItem(String todoItem) {
        this.todoItem = todoItem;
    }

    public TodoItem() {
    }

    public String getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(String todoItem) {
        this.todoItem = todoItem;
    }

    public Long getId() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

}
