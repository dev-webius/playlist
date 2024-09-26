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

        <c:choose>
            <c:when test="${agent.matches('(tablet|mac)')}">
                <c:set var="wide" value="12" />
                <c:set var="left" value="8" />
                <c:set var="right" value="4" />
            </c:when>
            <c:otherwise>
                <c:set var="wide" value="10" />
                <c:set var="left" value="7" />
                <c:set var="right" value="3" />
            </c:otherwise>
        </c:choose>

        <!-- Content Area -->
        <div class="body">
            <div class="container-fluid" style="margin-top: 3rem; margin-bottom: 5rem">
                <div class="row">
                    <div class="col col-12 col-lg-${wide} mx-auto">
                        <div class="d-flex justify-content-start mb-3">
                            <div class="d-flex">
                                <a class="btn btn-light-border" href="/play/${listId}" title="뒤로가기"><i class="fas fa-arrow-left"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-12 col-lg-${left} ml-auto">
                        <div class="row mb-3">
                            <div class="col col-12 px-0 px-lg-3">
                                <div>
                                    <div class="embed-responsive embed-responsive-16by9">
                                        <div class="embed-responsive-item">
                                            <c:choose>
                                                <c:when test="${play.platform == 'Youtube'}">
                                                    <div id="player"></div>
                                                    <%@include file="/WEB-INF/views/static/modules/api_youtube.jsp"%>
                                                </c:when>
                                                <c:when test="${play.platform == 'Naver'}">
                                                    <div id="player"></div>
                                                    <%@include file="/WEB-INF/views/static/modules/api_naver.jsp"%>
                                                </c:when>
                                                <c:when test="${play.platform == 'Kakao'}">
                                                    <video id="player" autoplay controls>
                                                        <source src="${play.videoUrl}" type="video/mp4">
                                                    </video>
                                                    <%@include file="/WEB-INF/views/static/modules/api_kakao.jsp"%>
                                                </c:when>
                                                <c:otherwise>
                                                    <p>해당 플랫폼은 지원되지 않습니다.</p>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-12">
                                <div class="d-flex flex-column flex-lg-row justify-content-between align-items-start px-lg-3">
                                    <div class="d-flex flex-column pr-lg-3">
                                        <h5 class="mb-0" style="word-break: break-all;">${video.name}</h5>
                                    </div>
                                    <div class="d-none d-lg-flex" style="min-width: 125px">
                                        <a class="btn btn-playlist" href="${video.url}" target="_blank" title="웹으로 실행"><i class="fas fa-location-arrow"></i> 웹에서 보기</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row d-flex d-lg-none">
                            <div class="col col-12 px-0">
                                <hr />
                            </div>
                        </div>
                    </div>
                    <div class="col col-12 col-lg-${right} mr-auto">
                        <div class="row">
                            <div class="col col-12 px-0 px-lg-3">
                                <ul class="table table-hover mb-0" data-role="playlist-mini">
                                    <c:forEach items="${nodeList}" var="node">
                                        <li class="mb-3 mb-lg-0">
                                            <a class="d-flex flex-column flex-xl-row justify-content-between<c:if test="${nodeId == node.idx}"> active</c:if>" href="/play/${listId}/view/${node.idx}">
                                                <div class="" data-column="thumbnail">
                                                    <div class="embed-responsive embed-responsive-16by9">
                                                        <img class="embed-responsive-item" src="${node.thumb}" />
                                                        <c:choose>
                                                            <c:when test="${node.type == '1'}">
                                                                <span data-column="platform"><i class="btn-icon btn-youtube-icon"></i></span>
                                                            </c:when>
                                                            <c:when test="${node.type == '2'}">
                                                                <span data-column="platform"><i class="btn-icon btn-naver-icon"></i></span>
                                                            </c:when>
                                                            <c:when test="${node.type == '3'}">
                                                                <span data-column="platform"><i class="btn-icon btn-kakao-icon"></i></span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span data-column="platform"><i class="btn-icon"></i></span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                                <div class="px-3 py-2" data-column="detail">
                                                    <p class="mb-0">${node.name}</p>
                                                </div>
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
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
