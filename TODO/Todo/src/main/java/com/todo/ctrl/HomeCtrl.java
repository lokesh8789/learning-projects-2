package com.todo.ctrl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.todo.dao.TodoDao;
import com.todo.entities.ToDo;

@Controller
public class HomeCtrl {
	@Autowired
	TodoDao todoDao;
	@RequestMapping("/home")
	public String Home(Model m)
	{
		String str="home";
		m.addAttribute("page",str);
		List<ToDo> list = this.todoDao.getAll();
		m.addAttribute("todos",list);
		return "home";
	}
	@RequestMapping("/add")
	public String addTodo(Model m)
	{
		ToDo t=new ToDo();
		m.addAttribute("page","add");
		m.addAttribute("todo",t);
		return "home";
	}
	@RequestMapping(value="/saveTodo",method=RequestMethod.POST)
	public String saveTodo(@ModelAttribute("todo") ToDo t,Model m)
	{
		System.out.println(t);
		t.setTodoDate(new Date());
		this.todoDao.save(t);
		m.addAttribute("msg","Successfully Added..");
		return "home";
	}
}
