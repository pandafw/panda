package panda.dao.entity;

import java.util.ArrayList;
import java.util.List;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.FK;
import panda.dao.entity.annotation.ForeignKeys;
import panda.dao.entity.annotation.Join;
import panda.dao.entity.annotation.JoinColumn;
import panda.dao.entity.annotation.Joins;
import panda.dao.entity.annotation.PK;
import panda.lang.Objects;

@Comment("Score of student")
@ForeignKeys({@FK(target=Student.class, fields={"student"})})
@Joins({@Join(name="s", target=Student.class, keys={"student"}, refs={"id"})})
public class Score {
	@PK
	int student;

	@JoinColumn(name="s", field="name")
	String studentName;
	
	@PK
	String klass;

	@Column
	Integer key;

	@Column(notNull=true, defaults="0")
	Integer score;

	public int getStudent() {
		return student;
	}

	public void setStudent(int studentId) {
		this.student = studentId;
	}

	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * @param studentName the studentName to set
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getKlass() {
		return klass;
	}

	public void setKlass(String klassName) {
		this.klass = klassName;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("student", student)
				.append("studentName", studentName)
				.append("klass", klass)
				.append("key", key)
				.append("score", score)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCodes(student, klass, key, score);
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
				.append(key, rhs.key)
				.append(score, rhs.score)
				.append(studentName, rhs.studentName)
				.isEquals();
	}

	public Score() {
	}

	public Score(int i, int k, int s) {
		this.student = i;
		this.klass = "K" + k;
		this.key = s;
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

	public static List<Score> sums(int f, int t) {
		List<Score> l = new ArrayList<Score>();
		for (int i = f; i <= t; i++) {
			Score s = new Score();
			s.student = i;
			s.score = 0;
			for (int k = 1; k < 5; k++) {
				s.score += (i * 10 + k) * 10;
			}
			l.add(s);
		}
		return l;
	}
}
