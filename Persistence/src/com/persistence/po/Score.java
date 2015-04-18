package com.persistence.po;

import com.persistence.annotation.ForeignKeysAnnotation;
import com.persistence.annotation.PrimaryKeysAnnotation;
import com.persistence.annotation.TableAnnotation;

@TableAnnotation(table="t_score")
public class Score {
	@PrimaryKeysAnnotation(column="stu_id" , auto_increment=false , update=false)
	@ForeignKeysAnnotation(column="stu_id" , type="String" )
	private Student stu;
	
	@PrimaryKeysAnnotation(column="course_id" , auto_increment=false , update=false)
	@ForeignKeysAnnotation(column="course_id" , type="String" )
	private Course cou;
	
	private String score;

	public Student getStu() {
		return stu;
	}

	public void setStu(Student stu) {
		this.stu = stu;
	}

	public Course getCou() {
		return cou;
	}

	public void setCou(Course cou) {
		this.cou = cou;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cou == null) ? 0 : cou.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((stu == null) ? 0 : stu.hashCode());
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
		Score other = (Score) obj;
		if (cou == null) {
			if (other.cou != null)
				return false;
		} else if (!cou.equals(other.cou))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (stu == null) {
			if (other.stu != null)
				return false;
		} else if (!stu.equals(other.stu))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Score [stu=" + stu + ", cou=" + cou + ", score=" + score + "]";
	}

}
