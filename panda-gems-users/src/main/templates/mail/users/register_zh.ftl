谢谢您的注册 - <${action.getText('site-name')}>
<#-- 会員登録の送信メール内容-->
<p>欢迎注册我们的网站&lt;<@p.text name="site-name"/>&gt;。</p>

<p>请确认您的登录信息。</p>

<pre>
---------------------------------
姓名：${name?html}
邮件：${email?html}
密码：${password?html}
---------------------------------
</pre>
<br>

<p>The <@p.text name="site-name"/> Team</p>
<br>

