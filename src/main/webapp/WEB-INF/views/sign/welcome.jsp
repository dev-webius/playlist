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
                <div class="row mb-3">
                    <div class="col col-lg-7 mx-auto">
                        <img src="/res/img/playlist/sign-welcome.png" style="width: 100%" />
                    </div>
                </div>
                <div class="row mb-3 text-center">
                    <div class="col col-lg-8 mx-auto">
                        <h2 class="mb-3">회원가입을 축하합니다!</h2>
                        <p class="mb-2">플레이리스트에서 재생 목록을 만들고!</p>
                        <p class="mb-2">다양한 플랫폼의 동영상을 한번에 감상하세요!</p>
                    </div>
                </div>
                <div class="row text-center">
                    <div class="col col-lg-8 mx-auto">
                        <a class="btn btn-playlist" href="/login" style="font-size: 20px; color: #ffffff; padding: 0.5rem 4rem;">로그인 하러가기</a>
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
