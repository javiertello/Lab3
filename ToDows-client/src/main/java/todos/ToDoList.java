package todos;

import java.util.*;

public class ToDoList {
	private List<ToDo> list = new ArrayList<ToDo>();

	public List<ToDo> getList() {
		return list;
	}

	public void setList(List<ToDo> list) {
		this.list = list;
	}
	
	public void addToDo(ToDo td){
		list.add(td);
	}
}
