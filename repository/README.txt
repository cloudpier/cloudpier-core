-- REPOSITORY MANAGER configuration --
--------------------------------------

The repository manager reads its configuration from the file

${c4s_home}/c4s/config/repository.properties

where c4s_home is a system property, defined in the JAVA_OPTS or CATALINA_OPTS variable. For example, in Linux the definition will be:

export JAVA_OPTS="${JAVA_OPTS} -Dc4s_home=/where/c4s/will/be/created"

in Windows:

set JAVA_OPTS=%JAVA_OPTS% -Dc4s_home=/where/c4s/will/be/created

Usually this definition is put in the startup file of the servlet container.
If the c4s_home property is not defined, then the system will look for for the c4s directory in the current path.

When the repository manager is instantiated, the system check if the properties file exists and reads the configuration from it; if the file doesn't exist, then it will be created and filled with default values defined inside the repository module, in the file:

src/main/resources/META-INF/default-repository.properties

                         =====     =====

In the properties file you can find the:

- backend property: it defines if the backed of the repository should be on a file (FILE value) or on database (DATABASE value).

-fileBackend section: it defines the properties needed to implement the backend on a file. At the moment, the only property is the file name where to store repository content.

-dbBackend section: it defines all the properties needed to implement the repository backend on a mySQL database; the properties defined are:
  * serverName: the name of the server that host the database
  * portNumber: number of the port on which you can connect the database
  * databaseName: name of the database on which to connect
  * username: the user needed to connect to the database
  * password: the password needed to connect to the database



