<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>플레이리스트 :: 당신의 실행 목록을 기록하세요</title>
    <%@include file="/WEB-INF/views/static/modules/meta.jsp"%>
    <%@include file="/WEB-INF/views/static/modules/bootstrap.jsp"%>
    <%@include file="/WEB-INF/views/static/modules/common.jsp"%>
    <script src="/res/js/login.js"></script>
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
                    <div class="col col-8 col-lg-3 mx-auto">
                        <img class="mb-4" src="/res/img/playlist/logo.png" style="width: 100%" />
                        <p class="mb-3 text-center">당신의 실행 목록을 만드세요!</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-5 mx-auto">
                        <form action="/login" method="post" onsubmit="return verifyLogin();" autocomplete="off">
                            <div class="form-group">
                                <input type="text" class="form-control" id="id" name="id" placeholder="아이디" />
                                <span class="text-danger mt-2" id="id_desc"></span>
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" id="pw" name="pw" placeholder="암호" />
                                <span class="text-danger mt-2" id="pw_desc"></span>
                            </div>
                            <input type="hidden" name="url" value="${url}" />
                            <button id="submitBtn" class="btn btn-playlist btn-block" type="submit">로그인</button>
                            <a class="btn btn-kakao btn-block" href="/login/kakao.init?url=${url}"><span>카카오 로그인</span></a>
                            <a class="btn btn-naver btn-block" href="/login/naver.init?url=${url}"><span>네이버 로그인</span></a>
                            <a class="btn btn-google btn-block" href="/login/google.init?url=${url}"><span>구글 로그인</span></a>
                            <a class="btn btn-outline-secondary btn-block" href="/sign">회원가입</a>
                        </form>
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
