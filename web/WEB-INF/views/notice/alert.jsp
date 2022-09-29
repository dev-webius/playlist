<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>플레이리스트 :: 당신의 실행 목록을 기록하세요</title>
    <%@include file="/WEB-INF/views/static/modules/meta.jsp"%>
    <%@include file="/WEB-INF/views/static/modules/bootstrap.jsp"%>
    <%@include file="/WEB-INF/views/static/modules/common.jsp"%>
</head>
<body>
<div class="wrap">
    <div class="content">
        <!-- Header Area -->
        <c:import url="${hdr}" />

        <!-- Content Area -->
        <div class="body">
            <div class="container-lg" style="margin-top: 3rem; margin-bottom: 5rem">
                <script>
                    <c:if test="${not empty alert.message}">
                    alert("${alert.message}");
                    </c:if>
                    if ("${alert.status}" === "warn") {
                        history.go(-1);
                    } else {
                        location.href = "${alert.returnUrl}";
                    }
                </script>
            </div>
        </div>
    </div>

    <!-- Footer Area -->
    <c:import url="${ftr}" />
</div>
</body>
</html>
