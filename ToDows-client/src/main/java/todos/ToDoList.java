package todos;

import java.util.*;

public class ToDoList {
	private List<ToDo> list = new ArrayList<ToDo>();
	private int nextId = 1;

	
	/**
	 * The value of next unique identifier.
	 * @return the next unique identifier.
	 */
	public int getNextId() {
		return nextId;
	}

	public void setNextId(int nextId) {
		this.nextId = nextId;
	}
	
	public List<ToDo> getList() {
		return list;
	}

	public void setList(List<ToDo> list) {
		this.list = list;
	}
	
	public void addToDo(ToDo td){
		list.add(td);
	}
	public boolean removeToDo(String task) {
		int i = 0;
		int borrar = 0;
		boolean encontrado = false;

		for (ToDo t : list) {
			if (t.getTask().equals(task)) {
				borrar = i;
				encontrado = true;
			}
			i++;
		}
		if(encontrado){
			list.remove(borrar);
		}
		
		return encontrado;
	}
	
	/**
	 * Returns the old next identifier and increases the new value in one.
	 * @return an identifier.
	 */
	public int nextId() {
		int oldValue = nextId;
		nextId++;
		return oldValue;
	}
}
