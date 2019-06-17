会员信息更新  - <${action.getText('site-name')}>
<#-- 会員プロファイル変更の送信メール内容 -->
<p>${name?html}先生</p>

<p>谢谢您使用&lt;<@p.text name="site-name"/>&gt;。</p>

<p>请确认您所变更的信息。</p>

<pre>
---------------------------------
姓名：${name?html}
邮件：${email?html}
邮政编码：${(zipcode!'')?html}
住所：${(address!'')?html}
电话号码：${(phone!'')?html}
---------------------------------
</pre>
<br>

<p>The <@p.text name="site-name"/> Team</p>
<br>

