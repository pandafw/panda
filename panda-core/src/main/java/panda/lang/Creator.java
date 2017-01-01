package panda.lang;

public interface Creator<T> {

	T create(Object... args);
}
