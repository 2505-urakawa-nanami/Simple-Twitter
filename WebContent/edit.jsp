<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>編集画面</title>
<link href="./css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="main-contents">
		<h1>つぶやきの編集</h1>

		<form action="edit" method="post">
			<input type="hidden" name="message_id" value="${message.id}"/>
			<p>つぶやき</p>
			<textarea name="text" cols="100" rows="5" class="tweet-box">${message.text}</textarea>
					<br /> <input type="submit" value="更新">（140文字まで）
		</form>
		<p><a href="./">戻る</a></p>
		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<c:forEach items="${errorMessages}" var="errorMessage">
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
			<c:remove var="errorMessages" scope="session" />
		</c:if>
		<div class="copyright">Copyright(c)YourName</div>
	</div>
</body>
</html>