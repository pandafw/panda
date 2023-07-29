<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta property="s:base" content="${base!}"/>
<meta property="s:statics" content="${statics!}"/>
<title><#if title?has_content>${title} | </#if><@p.text name="site-name"/></title>
<link rel="shortcut icon" href="${base}/favicon.ico"/>
<@p.link css="true" panda="true" stylesheets=(vars.stylesheets)![] version="${(s['prj.revision'])!}"/>
<link href="${base}/site/site.css?v=${(s['prj.revision'])!}" rel="stylesheet" type="text/css"/>
