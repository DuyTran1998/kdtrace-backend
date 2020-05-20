<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>QR Code</title>
</head>
<body>

<h3>Product List</h3>
<table border="1" cellpadding="2" cellspacing="2">
    <tr>
        <th>Id</th>
        <th>Code</th>
        <th>Ower</th>
        <th>Status</th>
        <th>BarCode</th>
        <th>OTP</th>
    </tr>
    <c:forEach var="qrCode" items="${qrCodes}">
        <tr>
            <td>${qrCode.id }</td>
            <td>${qrCode.code }</td>
            <td>${qrCode.ower }</td>
            <td>${qrCode.statusQRCode}</td>
            <td>
                <img src="http://localhost:8080/process/qrcode/${qrCode.code}" width="200" height="200">
            </td>
            <td>${qrCode.otp}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>