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
            <!-- 1140px -->
            <div class="container-lg" style="margin-top: 3rem; margin-bottom: 5rem">
                <div class="row text-bold text-center">
                    <div class="col col-12 display-2 mt-5 mb-2 text-primary"><i class="fas fa-exclamation-circle"></i></div>
                    <div class="col col-12 display-2 mb-2">500</div>
                </div>
                <div class="row">
                    <div class="col col-12">
                        <h4 class="text-center">서버에 문제가 발생했습니다.</h4>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer Area -->
    <c:import url="${ftr}" />
</div>
</body>
</html>
