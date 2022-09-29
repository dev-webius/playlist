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
            <div class="container-fluid d-none d-lg-block px-0" style="background: url('/res/img/playlist/banner.png') no-repeat; background-size: contain;">
                <div class="d-flex justify-content-center align-items-center">
                    <div class="container position-absolute">
                        <h2 class="text-bold mx-3 mb-3 mb-xl-4">다양한 플랫폼의 영상을 한번에!</h2>
                        <h5 class="mx-3 mb-4 mb-xl-5" style="font-weight: 300; line-height: 1.5">내가 보고싶었던 영상들을 모아 <br class="d-none d-xl-inline" />나만의 플레이리스트를 만들어 보세요!</h5>
                        <a class="btn btn-dark text-decoration-none mx-3" href="/play/write" style="border-radius: 2.5rem; font-size: 1.3rem; width: 250px;">지금 만들어보기</a>
                    </div>
                    <div class="invisible">
                        <img src="/res/img/playlist/banner.png" style="width: 100%" />
                    </div>
                </div>
            </div>
            <div class="container-lg" style="margin-top: 3rem; margin-bottom: 5rem">
                <div class="row mb-3">
                    <div class="col col-12">
                        <h3 class="text-center">최근 본 영상</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-12 px-0 px-lg-3">
                        <div class="d-flex flex-column flex-lg-row" style="justify-content: space-evenly">
                            <c:choose>
                                <c:when test="${not empty list || not empty play}">
                                    <c:if test="${not empty list}">
                                        <div class="col col-12 col-lg-3 px-0 px-lg-3" data-role="playlist-main">
                                            <a class="d-flex flex-column text-decoration-none text-body" href="/play/${list.idx}">
                                                <div class="flex-fill border" data-column="thumbnail">
                                                    <div class="embed-responsive embed-responsive-16by9">
                                                        <img class="embed-responsive-item" src="/res/img/playlist/list-logo.png" />
                                                    </div>
                                                </div>
                                                <div class="flex-fill p-2" data-column="detail">
                                                    <h5 class="text-bold text-center">나의 플레이리스트</h5>
                                                    <h6 class="text-bold text-center">${list.title}</h6>
                                                </div>
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty play}">
                                        <c:forEach items="${play}" var="playItem">
                                            <div class="col col-12 col-lg-3 px-0 px-lg-3" data-role="playlist-main">
                                                <a class="d-flex flex-column text-decoration-none text-body" href="/play/${playItem.list.idx}/view/${playItem.node.idx}">
                                                    <div class="flex-fill border" data-column="thumbnail">
                                                        <div class="embed-responsive embed-responsive-16by9">
                                                            <img class="embed-responsive-item" src="${playItem.node.thumb}" />
                                                            <c:choose>
                                                                <c:when test="${playItem.node.type == '1'}">
                                                                    <span data-column="platform"><i class="btn-icon btn-youtube-icon"></i></span>
                                                                </c:when>
                                                                <c:when test="${playItem.node.type == '2'}">
                                                                    <span data-column="platform"><i class="btn-icon btn-naver-icon"></i></span>
                                                                </c:when>
                                                                <c:when test="${playItem.node.type == '3'}">
                                                                    <span data-column="platform"><i class="btn-icon btn-kakao-icon"></i></span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span data-column="platform"><i class="btn-icon"></i></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </div>
                                                    <div class="flex-fill p-2" data-column="detail">
                                                        <p>${playItem.node.name}</p>
                                                    </div>
                                                </a>
                                            </div>
                                        </c:forEach>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <p class="mx-auto">최근 시청한 영상이 없습니다.</p>
                                </c:otherwise>
                            </c:choose>
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
