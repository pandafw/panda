#!/bin/bash -ex

# npm install -g uglify-js clean-css-cli

BASEDIR=$(dirname $(readlink -f $0))
HTMLDIR=$BASEDIR/src/html/panda/html

minjs() {
	echo --------------------------------------
	echo --  minify js: $1
	uglifyjs $1.js --warn --compress --mangle -o $1.min.js
	echo "
//# sourceMappingURL=$1.min.js.map" >> $1.min.js
}

mincss() {
	echo --------------------------------------
	echo --  minify css: $1
	cleancss -d -o $1.min.css $1.css
}

cd $HTMLDIR/tablesorter/
mincss jquery.ui.tablesorter

cd $HTMLDIR/datetimepicker/
mincss bootstrap-datetimepicker

cd $HTMLDIR/jquery/css/
cat jquery.*.css > jquery-plugins.css
mincss jquery-plugins

cd $HTMLDIR/panda/css/
cat ui.*.css > panda.css
mincss panda


cd $HTMLDIR/hammer/
minjs jquery.ui.hammer

cd $HTMLDIR/meiomask/
minjs jquery.ui.meio.mask

cd $HTMLDIR/mousewheel/
minjs jquery.ui.mousewheel

cd $HTMLDIR/notifyjs/
minjs jquery.ui.notify

cd $HTMLDIR/tablesorter/
minjs jquery.ui.tablesorter

cd $HTMLDIR/datetimepicker/
minjs bootstrap-datetimepicker

cd $HTMLDIR/respondjs/
minjs respond

cd $HTMLDIR/corejs/
cat core.*.js > corejs.js
minjs corejs

cd $HTMLDIR/jquery/js/
cat jquery.*.js > jquery-plugins.js
minjs jquery-plugins

cd $HTMLDIR/panda/js/
cat ui.*.js > panda.js
minjs panda


echo --------------------------------------
echo DONE.
echo
