package ${package};

import panda.io.Files;
import webapp.runner.launch.Main;

public class AppMain {
	public static void main(String[] args) {
		try {
			Files.makeDirs("web/WEB-INF/_sqlite");

			System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow", "|{}&");
			Main.main(new String[] { 
					"--port", "8080",
					"-ArelaxedPathChars=[]|{}&",
					"-ArelaxedQueryChars=[]|{}&",
					"--temp-directory", "out/tomcat",
					"--uri-encoding", "UTF-8",
					"--use-body-encoding-for-uri",
					"web" });
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
