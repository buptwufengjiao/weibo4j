package com.jungstudy;

/**
 * 边类的定义
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.commons.collections15.Factory;

import com.jungstudy.UserVertex;import org.apache.commons.lang.builder.HashCodeBuilder;
public class RelationLink implements List {
	static int sid = -1;
	int id;
	UserVertex userVertex;
	String id1id2;
    int weight;

	RelationLink(UserVertex userVertex,String ids,int weight) {
		this.id = ++sid;
		this.userVertex=userVertex;
		this.id1id2 = ids;
		this.weight=weight;
	}

	
	public RelationLink(int i) {
		// TODO Auto-generated constructor stub
	}


	public static int getSid() {
		return sid;
	}


	public static void setSid(int sid) {
		RelationLink.sid = sid;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public UserVertex getUserVertex() {
		return userVertex;
	}


	public void setUserVertex(UserVertex userVertex) {
		this.userVertex = userVertex;
	}


	public String getId1id2() {
		return id1id2;
	}


	public void setId1id2(String id1id2) {
		this.id1id2 = id1id2;
	}


	public int getWeight() {
		return weight;
	}


	public void setWeight(int weight) {
		this.weight = weight;
	}


	public String toString(){
		//return this.id1id2+"("+id+")";
		return this.weight+"("+id+")";
	}
	
	static class RelationLinkFactory implements Factory<RelationLink> {
		 static RelationLinkFactory instance = new RelationLinkFactory();

		public RelationLink create() {
			return new RelationLink(new UserVertex("", "", new Vector<String>()), "", 1);
		}

		public static  RelationLinkFactory getInstance() {
			return instance;
		}

}

	public boolean add(Object e) {
		// TODO Auto-generated method stub
		return false;
	}


	public void add(int index, Object element) {
		// TODO Auto-generated method stub
		
	}


	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean addAll(int index, Collection c) {
		// TODO Auto-generated method stub
		return false;
	}


	public void clear() {
		// TODO Auto-generated method stub
		
	}


	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean containsAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}


	public Object get(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}


	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}


	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}


	public ListIterator listIterator() {
		// TODO Auto-generated method stub
		return null;
	}


	public ListIterator listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}


	public Object remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean removeAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean retainAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}


	public Object set(int index, Object element) {
		// TODO Auto-generated method stub
		return null;
	}


	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}


	public List subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}


	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}


	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-592336469, -200953953).appendSuper(
				super.hashCode()).append(this.id1id2).append(this.userVertex)
				.append(this.weight).append(this.id).toHashCode();
	}

}