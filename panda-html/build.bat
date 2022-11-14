@echo off

REM npm install -g uglify-js clean-css-cli

set BASEDIR=%~dp0
set HTMLDIR=%BASEDIR%\src\html\panda\html\

cd /d %HTMLDIR%\lightbox\
call :mincss jquery.ui.lightbox

cd /d %HTMLDIR%\tablesorter\
call :mincss jquery.ui.tablesorter

cd /d %HTMLDIR%\datetimepicker\
call :mincss bootstrap-datetimepicker

cd /d %HTMLDIR%\jquery\css\
copy /b jquery.*.css jquery-plugins.css 
call :mincss jquery-plugins

cd /d %HTMLDIR%\panda\css\
copy /b ui.*.css panda.css 
call :mincss panda


cd /d %HTMLDIR%\hammer\
call :minjs jquery.ui.hammer

cd /d %HTMLDIR%\lightbox\
call :minjs jquery.ui.lightbox

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

cd /d %HTMLDIR%\jquery\js\
copy /b jquery.*.js jquery-plugins.js 
call :minjs jquery-plugins

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
call uglifyjs.cmd %1.js --warn --compress --mangle --output %1.min.js
echo.>> %1.min.js
echo //# sourceMappingURL=%1.min.js.map>> %1.min.js
exit /b

:mincss
echo --------------------------------------
echo --  minify css: %1
call cleancss.cmd -d -o %1.min.css %1.css
exit /b
