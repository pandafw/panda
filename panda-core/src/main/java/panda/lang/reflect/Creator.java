package panda.lang.reflect;

public interface Creator<T> {

	T create(Object... args);
}
