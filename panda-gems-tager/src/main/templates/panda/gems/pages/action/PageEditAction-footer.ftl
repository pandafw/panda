<script type="text/javascript">
	function onPageLoad_Thumbnail() {
		var mp = "<@p.url action="%{!!(b.media_path)|||'/media'}"/>";
		var $t = $('#pages_thumbnail').hide();
		var $d = $('<img id="pages_thumbnail_view" class="p-frame p-cpointer">');
		
		var fu = function() {
			$d.attr({
				'src': mp + '/thumb/' + $t.val(),
				'title': $t.val()
			});
		};

		fu();
		$t.next('.p-viewfield').hide();
		$t.parent().append($d);
		$t.change(fu);

		if ($t.attr('type') != 'hidden') {
			$('#pages_thumbnail_view').click(function() {
				$.popup({
						id: 'pages_content_media_popup',
						url: mp + '/browse.popup'
					})
					.show({
						id: 'pages_content_media_popup',
						trigger: this,
						popover: true,
						onpopup: function() {
							media_set_method('select_popup');
						},
						onhide: function() {
							media_set_method('browse_popup');
						},
						callback: function(sd) {
							$("#pages_thumbnail").changeValue(sd.slug);
							$.popup().hide();
						}
					});
			});
		}
	}
</script>
