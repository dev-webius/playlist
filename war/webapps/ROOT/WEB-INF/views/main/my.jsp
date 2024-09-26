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
                <div class="row">
                    <div class="col col-lg-6 mx-auto">
                        <h3 class="text-bold text-center mb-3">내 정보</h3>
                        <div class="d-flex flex-row justify-content-end" style="margin-bottom: 1rem">
                            <a class="btn btn-light-border" href="/my/edit" title="내 정보 수정하기"><i class="fas fa-cog"></i></a>
                        </div>
                        <hr/>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-6 mx-auto">
                        <div class="form-group">
                            <p>닉네임</p>
                            <p class="form-control">${alias}</p>
                        </div>
                        <div class="form-group">
                            <p>아이디</p>
                            <p class="form-control">
                                <c:choose>
                                    <c:when test="${not empty userdata.id}">
                                        ${userdata.id}
                                    </c:when>
                                    <c:otherwise>
                                        생성되지 않았습니다.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="form-group">
                            <p>구글 ID</p>
                            <p class="form-control">
                                <c:choose>
                                    <c:when test="${not empty userdata.google}">
                                        ${userdata.google}
                                    </c:when>
                                    <c:otherwise>
                                        연동되지 않았습니다.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="form-group">
                            <p>네이버 ID</p>
                            <p class="form-control">
                                <c:choose>
                                    <c:when test="${not empty userdata.naver}">
                                        ${userdata.naver}
                                    </c:when>
                                    <c:otherwise>
                                        연동되지 않았습니다.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="form-group">
                            <p>카카오 ID</p>
                            <p class="form-control">
                                <c:choose>
                                    <c:when test="${not empty userdata.kakao}">
                                        ${userdata.kakao}
                                    </c:when>
                                    <c:otherwise>
                                        연동되지 않았습니다.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
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
