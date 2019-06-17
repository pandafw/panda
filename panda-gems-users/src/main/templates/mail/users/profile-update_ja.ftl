個人情報更新  - <${action.getText('site-name')}>
<#-- 会員プロファイル変更の送信メール内容 -->
${name?html}様

<p>&lt;<@p.text name="site-name"/>&gt;にご愛顧ありがとうございます。</p>

<p>下記の内容を変更しました。ご確認ください。</p>

<pre>
---------------------------------
姓名：${name?html}
メール：${email?html}
郵便番号：${(zipcode!'')?html}
住所：${(address!'')?html}
電話番号：${(phone!'')?html}
---------------------------------
</pre>
<br>

<p>The <@p.text name="site-name"/> Team</p>
<br>

