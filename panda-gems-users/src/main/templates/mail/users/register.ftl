Welcome to <${action.getText('site-name')}>
<#-- mail body -->
<p>Welcome to &lt;<@p.text name="site-name"/>&gt;.</p>

<p>Thanks for your registration.</p>

<pre>
---------------------------------
Name: ${name?html}
Email: ${email?html}
Password: ${password?html}
---------------------------------
</pre>
<br>

<p>Sincerely,</p>
<p>The <@p.text name="site-name"/> Team</p>
<br>

