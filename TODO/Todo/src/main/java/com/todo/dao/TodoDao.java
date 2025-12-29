package com.todo.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.todo.entities.ToDo;
@Component
public class TodoDao {
	@Autowired
	HibernateTemplate hibernateTemplate;
	@Transactional
	public int save(ToDo t)
	{
		return (Integer) this.hibernateTemplate.save(t);
	}
	public List<ToDo> getAll()
	{
		return this.hibernateTemplate.loadAll(ToDo.class);
	}
}
