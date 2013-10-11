package panda.dao.entity;

import java.util.ArrayList;
import java.util.List;

import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.PK;
import panda.lang.Objects;

@Comment("Score of student")
public class Score {
	@PK
	int student;

	@PK
	String klass;

	Integer score;

	public int getStudent() {
		return student;
	}

	public void setStudent(int studentId) {
		this.student = studentId;
	}

	public String getKlass() {
		return klass;
	}

	public void setKlass(String klassName) {
		this.klass = klassName;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("student", student)
				.append("klass", klass)
				.append("score", score)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.append(student)
				.append(klass)
				.append(score)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Score rhs = (Score)obj;
		return Objects.equalsBuilder()
				.append(student, rhs.student)
				.append(klass, rhs.klass)
				.append(score, rhs.score)
				.isEquals();
	}

	public Score() {
	}

	public Score(int i, int k, int s) {
		this.student = i;
		this.klass = "K" + k;
		this.score = s * 10;
	}

	public static Score create(int i, int k, int s) {
		return new Score(i, k, s);
	}

	public static List<Score> creates(int f, int t) {
		List<Score> l = new ArrayList<Score>();
		for (int i = f; i <= t; i++) {
			for (int k = 1; k < 5; k++) {
				l.add(new Score(i, k, i * 10 + k));
			}
		}
		return l;
	}
}
