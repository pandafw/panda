 ${artifactId}
=========================================

 build
----------
> mvn clean package -Dlog.hostname=$HOSTNAME -Dlog.slack.channel=develop -Dlog.slack.webhook=https://hooks.slack.com/services/XXXXXX


 deploy (debug)
----------------
> mvn antrun:run@deploy -DAPPNAME=${artifactId}


 deploy (release)
-------------------
> mvn antrun:run@deploy -DAPPNAME=${artifactId} -DRELEASE=true


 deploy (release & use cdn)
----------------------------
> mvn antrun:run@deploy -DAPPNAME=${artifactId} -DRELEASE=true -DUSECDN=true



 tomcat server.xml settings
------------------------------
	<Connector port="8080" redirectPort="8443" protocol="HTTP/1.1"
		maxThreads="200" connectionTimeout="20000"
		URIEncoding="UTF-8"
		/>

	<Context path="" reloadable="true" docBase="/app/${artifactId}/web" allowLinking="true">
		<Resource name="jdbc/${artifactId}" auth="Container" type="javax.sql.DataSource"
			maxActive="200" maxIdle="20" maxWait="10000"
			username="XXXXXX" password="YYYYYY"
			factory="org.apache.commons.dbcp.BasicDataSourceFactory"
			driverClassName="com.mysql.jdbc.Driver"
			url="jdbc:mysql://localhost:3306/${artifactId}?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=utf8"
			validationQuery="SELECT 1"
		/>
		<Manager pathname=""/>
	</Context>
