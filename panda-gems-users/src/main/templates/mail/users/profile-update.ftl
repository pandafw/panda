Profile updated  - <${action.getText('site-name')}>
<#-- mail body -->
<p>Hi, ${name?html}</p>

<p>Thanks for using &lt;<@p.text name="site-name"/>&gt;.

<p>Your profile is updated as below.</p>

<pre>
---------------------------------
Name: ${name?html}
Email: ${email?html}
Zip code: ${(zipcode!'')?html}
Address: ${(address!'')?html}
TEL: ${(phone!'')?html}
---------------------------------
</pre>
<br>

<p>Sincerely,</p>
<p>The <@p.text name="site-name"/> Team</p>
<br>

