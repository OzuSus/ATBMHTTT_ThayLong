<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 12/10/2024
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <!--Các thư viện hỗ trợ-->
  <!--Font Awesome-->
  <link rel="stylesheet" href="assets/fontIcon/fontawesome-free-6.4.2-web/css/all.min.css">
  <link rel="stylesheet" href="assets/bootstrap/bootstrap-grid.min.css">
  <!--Favicon-->
  <link rel="apple-touch-icon" sizes="180x180" href="assets/favicon/apple-touch-icon.png">
  <link rel="icon" type="image/png" sizes="32x32" href="assets/favicon/favicon-32x32.png">
  <link rel="icon" type="image/png" sizes="16x16" href="assets/favicon/favicon-16x16.png">
  <link rel="manifest" href="assets/favicon/site.webmanifest">
  <!--Web font-->
  <link rel="stylesheet" href="assets/font/webfonts/Montserrat.css">

  <link rel="stylesheet" href="assets/bootstrap/bootstrap-grid.min.css">
  <link rel="stylesheet" href="assets/fontIcon/fontawesome-free-6.4.2-web/css/all.min.css">
  <link rel="manifest" href="assets/favicon/site.webmanifest">
  <link rel="stylesheet" href="assets/font/webfonts/Montserrat.css">
  <link rel="stylesheet" href="assets/css/reset.css">
  <link rel="stylesheet" href="assets/css/base.css">

  <link rel="stylesheet" href="assets/css/account.css">
  <title>Report Key</title>

    <style>
        .hidden {
            display: none;
        }

        .popup {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: #fff;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 20px;
            border-radius: 8px;
            z-index: 1000;
        }

        .popup-content {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }

        .alert {
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 5px;
        }

        .alert-success {
            color: #155724;
            background-color: #d4edda;
            border-color: #c3e6cb;
        }

        .alert-danger {
            color: #721c24;
            background-color: #f8d7da;
            border-color: #f5c6cb;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<main class="main">
    <div class="container-xl">
        <div class="row">
            <div class="col-3">
                <div class="service__list">
                    <a class="service__item" href="Account">Chỉnh sửa tài khoản</a>
                    <a class="service__item" href="ChangePassword">Đổi mật khẩu</a>
                    <a class="service__item" href="PurchaseHistory">Lịch sử mua hàng</a>
                    <a class="service__item service__item--clicked">report Key</a>
                </div>
            </div>
            <div class="col-9">
                <section class="service__section service__section--show">
                    <form action="${pageContext.request.contextPath}/report-key" method="post">
                        <h1 class="title">Báo cáo lộ Key</h1>
                        <div>
                            <c:if test="${not empty message}">
                                <p class="alert alert-success">${message}</p>
                            </c:if>
                            <c:if test="${not empty error}">
                                <p class="alert alert-danger">${error}</p>
                            </c:if>
                        </div>
                        <div class="save save__changePass">
                            <button type="submit" class="btn btn-primary" name="action" value="resetKey">Quên Key</button>
                        </div>
                        <div class="save save__changePass">
                            <button type="button" class="btn btn-secondary" onclick="showPasswordPopup()">Report Key</button>
                        </div>
                    </form>
                </section>
            </div>
        </div>
    </div>
</main>


<!-- Popup nhập Key mật khẩu -->
<div id="passwordPopup" class="popup hidden">
    <div class="popup-content">
        <h2>Nhập mật khẩu</h2>
        <form action="${pageContext.request.contextPath}/report-key" method="post">
            <input type="hidden" name="action" value="resetKey">

            <div class="form-group">
                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Xác nhận</button>
                <button type="button" class="btn btn-secondary" onclick="hidePasswordPopup()">Hủy</button>
            </div>
        </form>
    </div>
</div>

<%@include file="footer.jsp" %>

<script>
    function showPasswordPopup() {
        const popup = document.getElementById("passwordPopup");
        popup.classList.remove("hidden");
    }

    function hidePasswordPopup() {
        const popup = document.getElementById("passwordPopup");
        popup.classList.add("hidden");
    }
</script>
</body>
</html>
