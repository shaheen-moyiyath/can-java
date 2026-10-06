@REM ----------------------------------------------------------------------------
@REM Maven Wrapper script for Windows
@REM ----------------------------------------------------------------------------
@echo off
set ERROR_CODE=0

set "MAVEN_HOME=%~dp0.tools\apache-maven-3.9.6"
if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    call "%MAVEN_HOME%\bin\mvn.cmd" %*
    goto end
)

REM Fallback to PATH mvn if not found in .tools
mvn %*

:end
exit /B %ERROR_CODE%
