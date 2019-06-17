获取密码 - <${action.getText('site-name')}>
<#-- 会員パスワード変更の送信メール内容 -->
<p>${name?html}先生</p>

<p>谢谢您使用&lt;<@p.text name="site-name"/>&gt;。</p>

<pre>
您的密码已经被重置了。
密码: ${password?html}
</pre>
<br>

<p>The <@p.text name="site-name"/> Team</p>
<br>

