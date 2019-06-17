会員登録ありがとうございます - <${action.getText('site-name')}>
<#-- 会員登録の送信メール内容-->
<p>&lt;<@p.text name="site-name"/>&gt;にご登録ありがとうございます。</p>

<p>下記の内容を登録しました。ご確認ください。</p>

<pre>
---------------------------------
姓名： ${name?html}
メール： ${email?html}
パスワード： ${password?html}
---------------------------------
</pre>
<br>

<p>The <@p.text name="site-name"/> Team</p>
<br>

