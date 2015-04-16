package panda.lang.reflect.testbed;

public class Horse {
	private String name;

	protected String getName() {
		return name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	public static class BlackHorse extends Horse {
		private String name;

		protected String getName() {
			return name;
		}

		protected void setName(String name) {
			this.name = name;
		}
	}
}
