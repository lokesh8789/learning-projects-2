package com.tech.blog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LikeDao {
	Connection con;
	public LikeDao(Connection con) {
		this.con=con;
	}
	public boolean insertLike(int pid,int uid)
	{
		boolean f=false;
		try {
			String q="insert into liked(pid,uid) values(?,?)";
			PreparedStatement ps=this.con.prepareStatement(q);
			ps.setInt(1, pid);
			ps.setInt(2, uid);
			ps.executeUpdate();
			f=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	public int countLikeOnPost(int pid)
	{
		int count=0;
		try {
			String q = "select count(*) from liked where pid=?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, pid);
			ResultSet set = ps.executeQuery();
			if(set.next())
			{
				count=set.getInt("count(*)");
				System.out.println(count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	public boolean isLikedByUser(int pid,int uid)
	{
		boolean f=false;
		try {
			String q="select * from liked where pid=? and uid=?";
			PreparedStatement ps=con.prepareStatement(q);
			ps.setInt(1,pid);
			ps.setInt(2,uid);
			ResultSet set = ps.executeQuery();
			if(set.next())
			{
				f=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	public boolean deleteLike(int pid,int uid)
	{
		boolean f=false;
		try {
			String q="delete from liked where pid=? and uid=?";
			PreparedStatement ps=con.prepareStatement(q);
			ps.setInt(1, pid);
			ps.setInt(2, uid);
			ps.executeUpdate();
			f=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
}
