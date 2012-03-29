package jp.rough_diamond.framework.cache;

//Java Wrapper
public class Cacher {
	public static final String DI_KEY = ObjectBank$.MODULE$.DI_KEY();
	public static ObjectBank get() {
		return ObjectBank$.MODULE$.get();
	}
}
