package com.persistence.po;

import com.persistence.annotation.PrimaryKeysAnnotation;
import com.persistence.annotation.TableAnnotation;

@TableAnnotation(table="t_student")
public class Student {
	
	@PrimaryKeysAnnotation(column="stu_id" , auto_increment=false , update=false)
	private String stu_id;
	private String stu_name;
	public String getStu_id() {
		return stu_id;
	}
	public void setStu_id(String stu_id) {
		this.stu_id = stu_id;
	}
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stu_id == null) ? 0 : stu_id.hashCode());
		result = prime * result
				+ ((stu_name == null) ? 0 : stu_name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (stu_id == null) {
			if (other.stu_id != null)
				return false;
		} else if (!stu_id.equals(other.stu_id))
			return false;
		if (stu_name == null) {
			if (other.stu_name != null)
				return false;
		} else if (!stu_name.equals(other.stu_name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [stu_id=" + stu_id + ", stu_name=" + stu_name + "]";
	}

}
