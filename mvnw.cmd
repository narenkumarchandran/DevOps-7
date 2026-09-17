@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF)
@REM Maven Wrapper startup batch script, version 3.2.0
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET "MVN_CMD=mvn") ELSE (SET "MVN_CMD=%__MVNW_ARG0_NAME__%")
@SET DP0=%~dp0
@SET MAVEN_WRAPPER_JAR="%DP0%.mvn\wrapper\maven-wrapper.jar"
@SET MAVEN_WRAPPER_PROPERTIES="%DP0%.mvn\wrapper\maven-wrapper.properties"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@FOR /F "usebackq tokens=1,2 delims==" %%A IN (%MAVEN_WRAPPER_PROPERTIES%) DO (
    @IF "%%A"=="distributionUrl" SET DISTRIBUTION_URL=%%B
)

@SET JAVA_HOME_CANDIDATE=%JAVA_HOME%
@IF "%JAVA_HOME_CANDIDATE%"=="" SET JAVA_HOME_CANDIDATE=%ProgramFiles%\Java\jdk-22

@SET JAVA_EXE=java
@IF EXIST "%JAVA_HOME_CANDIDATE%\bin\java.exe" SET JAVA_EXE="%JAVA_HOME_CANDIDATE%\bin\java.exe"

@"%JAVA_EXE%" -classpath %MAVEN_WRAPPER_JAR% %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
