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
                <div class="row mb-4">
                    <div class="col col-12 col-lg-8 mx-auto">
                        <h3 class="text-bold text-center" style="word-break: break-all">'${resultData.query}' 검색 결과</h3>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col col-12 col-lg-8 mx-auto mb-2">
                        <div class="d-flex justify-content-center justify-content-lg-start">
                            <a class="p-2 text-body text-decoration-none<c:if test="${resultData.platform == 0}"> active</c:if>" data-role="search-link" href="/search?q=${resultData.query}"><h5>전체</h5></a>
                            <a class="p-2 text-body text-decoration-none<c:if test="${resultData.platform == 1}"> active</c:if>" data-role="search-link" href="/search?q=${resultData.query}&c=Youtube"><h5>유튜브</h5></a>
                            <a class="p-2 text-body text-decoration-none<c:if test="${resultData.platform == 2}"> active</c:if>" data-role="search-link" href="/search?q=${resultData.query}&c=Naver"><h5>네이버</h5></a>
                            <a class="p-2 text-body text-decoration-none<c:if test="${resultData.platform == 3}"> active</c:if>" data-role="search-link" href="/search?q=${resultData.query}&c=Kakao"><h5>카카오</h5></a>
                        </div>
                    </div>
                    <div class="col col-12 col-lg-8 mx-auto">
                        <p class="text-secondary text-center text-lg-left">${resultData.count}개의 검색 결과</p>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${resultData.count > 0}">
                        <div class="row mb-3">
                            <div class="col col-12 col-lg-8 mx-auto px-0 px-lg-3">
                                <ul class="table table-hover" data-role="playlist">
                                    <c:forEach items="${resultList}" var="result">
                                        <li class="mb-3 mb-lg-0">
                                            <a class="d-flex flex-column flex-lg-row flex-fill pr-lg-1" href="/play/${result.pidx}/view/${result.pbidx}">
                                                <div class="mr-lg-3 mb-2 mb-lg-0" data-column="thumbnail">
                                                    <div class="embed-responsive embed-responsive-16by9">
                                                        <img class="embed-responsive-item" src="${result.thumb}" />
                                                        <c:choose>
                                                            <c:when test="${result.type == '1'}">
                                                                <span data-column="platform"><i class="btn-icon btn-youtube-icon"></i></span>
                                                            </c:when>
                                                            <c:when test="${result.type == '2'}">
                                                                <span data-column="platform"><i class="btn-icon btn-naver-icon"></i></span>
                                                            </c:when>
                                                            <c:when test="${result.type == '3'}">
                                                                <span data-column="platform"><i class="btn-icon btn-kakao-icon"></i></span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span data-column="platform"><i class="btn-icon"></i></span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                                <div class="px-3 py-2 px-lg-0" data-column="detail">
                                                    <h6>${result.name}</h6>
                                                </div>
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row text-bold text-center mb-3">
                            <div class="col col-12 display-2 mt-5 mb-2 text-secondary"><i class="fas fa-exclamation-circle"></i></div>
                        </div>
                        <div class="row">
                            <div class="col col-12">
                                <h4 class="text-center text-secondary">검색 결과가 없습니다.</h4>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                <c:if test="${page.size > 1}">
                    <c:set var="pageBeginSet" value="${page.index}" />
                    <c:set var="pageEndSet" value="${page.index + 4}" />
                    <c:set var="pageBegin" value="${pageBeginSet}" />
                    <c:set var="pageEnd" value="${pageEndSet}" />

                    <c:if test="${resultData.platform == 0}">
                        <c:set var="prefix" value="?q=${resultData.query}" />
                    </c:if>
                    <c:if test="${resultData.platform > 0}">
                        <c:set var="prefix" value="?q=${resultData.query}&c=${resultData.platformName}" />
                    </c:if>

                    <div class="row">
                        <div class="col col-12 col-lg-8 mx-auto">
                            <c:if test="${pageBeginSet < 3}">
                                <c:forEach begin="${pageBeginSet}" end="2">
                                    <c:set var="pageBegin" value="${pageBegin + 1}" />
                                    <c:set var="pageEnd" value="${pageEnd + 1}" />
                                </c:forEach>
                            </c:if>
                            <c:if test="${pageBeginSet > 3 && (pageEndSet - 4) > page.size - 2}">
                                <c:forEach begin="${page.size - 1}" end="${pageEndSet - 4}">
                                    <c:set var="pageBegin" value="${pageBegin - 1}" />
                                    <c:set var="pageEnd" value="${pageEnd - 1}" />
                                </c:forEach>
                            </c:if>
                            <ul class="pagination pagination-sm justify-content-center pt-3">
                                <li class="page-item<c:if test="${page.index == 1}"> disabled</c:if>"><a class="page-link" href="/search${prefix}&pageIndex=${page.index - 1}">Prev</a></li>
                                <c:forEach begin="${pageBegin - 2}" end="${pageEnd - 2}" var="pageNum">
                                    <c:if test="${pageNum > 0 && pageNum <= page.size}">
                                        <li class="page-item<c:if test="${page.index == pageNum}"> active</c:if>"><a class="page-link" href="/search${prefix}&pageIndex=${pageNum}">${pageNum}</a></li>
                                    </c:if>
                                </c:forEach>
                                <li class="page-item<c:if test="${page.index == page.size}"> disabled</c:if>"><a class="page-link" href="/search${prefix}&pageIndex=${page.index + 1}">Next</a></li>
                            </ul>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Footer Area -->
    <c:import url="${ftr}" />
</div>
</body>
</html>
