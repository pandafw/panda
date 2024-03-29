@echo off

REM npm install -g uglify-js clean-css-cli

set BASEDIR=%~dp0
set HTMLDIR=%BASEDIR%\src\html\panda\html\

cd /d %HTMLDIR%\tablesorter\
call :mincss jquery.ui.tablesorter

cd /d %HTMLDIR%\datetimepicker\
call :mincss bootstrap-datetimepicker

cd /d %HTMLDIR%\plugins\css\
type jquery.*.css    >  plugins.css
type bootstrap.*.css >> plugins.css
type ui.*.css        >> plugins.css
call :mincss plugins

cd /d %HTMLDIR%\panda\css\
copy /b ui.*.css panda.css 
call :mincss panda


cd /d %HTMLDIR%\hammer\
call :minjs jquery.ui.hammer

cd /d %HTMLDIR%\meiomask\
call :minjs jquery.ui.meio.mask

cd /d %HTMLDIR%\mousewheel\
call :minjs jquery.ui.mousewheel

cd /d %HTMLDIR%\notifyjs\
call :minjs jquery.ui.notify

cd /d %HTMLDIR%\tablesorter\
call :minjs jquery.ui.tablesorter

cd /d %HTMLDIR%\datetimepicker\
call :minjs bootstrap-datetimepicker

cd /d %HTMLDIR%\respondjs\
call :minjs respond

cd /d %HTMLDIR%\corejs\
copy /b core.*.js corejs.js
call :minjs corejs

cd /d %HTMLDIR%\plugins\js\
type jquery.*.js    >  plugins.js
type bootstrap.*.js >> plugins.js
call :minjs plugins

cd /d %HTMLDIR%\panda\js\
copy /b ui.*.js panda.js 
call :minjs panda


echo --------------------------------------
echo DONE.

cd /d %BASEDIR%
exit /b


:minjs
echo --------------------------------------
echo --  minify js: %1
call uglifyjs.cmd %1.js --warn --compress --mangle --source-map url=%1.min.js.map -o %1.min.js
rem call uglifyjs.cmd %1.js --warn --compress --mangle --output %1.min.js
rem echo.>> %1.min.js
rem echo //# sourceMappingURL=%1.min.js.map>> %1.min.js
exit /b

:mincss
echo --------------------------------------
echo --  minify css: %1
call cleancss.cmd -d -o %1.min.css %1.css
exit /b
