package toDows;

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
}
