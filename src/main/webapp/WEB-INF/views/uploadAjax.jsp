<%--
  Created by IntelliJ IDEA.
  User: eunjikim
  Date: 2021/05/24
  Time: 4:04 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/resources/css/uploadAjax.css">
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/uploadAjax.js"></script>
    <title>Title</title>
</head>
<body>
<h1>Upload with Ajax</h1>

<div class="uploadDiv">
    <input type="file" name="uploadFile" multiple>
</div>

<button id="uploadBtn">Upload</button>

<div class="uploadResult">
    <ul>

    </ul>
</div>

<div class="bigPictureWrapper">
    <div class="bigPicture">

    </div>
</div>

</body>
</html>
