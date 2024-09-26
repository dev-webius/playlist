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
                <h3 class="text-bold text-center mb-3">공지사항</h3>
                <c:if test="${user.UID == '1'}">
                    <div class="d-flex flex-row justify-content-end">
                        <a class="btn btn-playlist btn-sm" href="/notice/write"><i class="fas fa-pen"></i> 글쓰기</a>
                    </div>
                </c:if>
                <c:choose>
                    <c:when test="${noticeList.size() > 0}">
                        <ul class="table table-hover text-center pt-3" data-role="notice">
                            <c:forEach items="${noticeList}" var="notice">
                                <li>
                                    <a class="p-2" href="/notice/${notice.nid}">
                                        <span class="pr-3" data-column="seq">${notice.nid}</span>
                                        <span class="text-left" data-column="subject">${notice.subject}</span>
                                        <span class="d-none d-sm-inline-block" data-column="author">${notice.alias}</span>
                                        <span class="d-none d-md-inline-block" data-column="cdate">${notice.cdate}</span>
                                        <span class="d-none d-lg-inline-block" data-column="view">${notice.view}</span>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center mb-3">
                            <p class="p-3">게시글이 없습니다.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
                <c:if test="${user.UID == '1'}">
                    <div class="d-flex flex-row justify-content-end">
                        <a class="btn btn-playlist btn-sm" href="/notice/write"><i class="fas fa-pen"></i> 글쓰기</a>
                    </div>
                </c:if>
                <c:if test="${page.size > 1}">
                    <c:set var="pageBeginSet" value="${page.index}" />
                    <c:set var="pageEndSet" value="${page.index + 4}" />
                    <c:set var="pageBegin" value="${pageBeginSet}" />
                    <c:set var="pageEnd" value="${pageEndSet}" />
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
                        <li class="page-item<c:if test="${page.index == 1}"> disabled</c:if>"><a class="page-link" href="/notice/?pageIndex=${page.index - 1}">Prev</a></li>
                        <c:forEach begin="${pageBegin - 2}" end="${pageEnd - 2}" var="pageNum">
                            <c:if test="${pageNum > 0 && pageNum <= page.size}">
                                <li class="page-item<c:if test="${page.index == pageNum}"> active</c:if>"><a class="page-link" href="/notice/?pageIndex=${pageNum}">${pageNum}</a></li>
                            </c:if>
                        </c:forEach>
                        <li class="page-item<c:if test="${page.index == page.size}"> disabled</c:if>"><a class="page-link" href="/notice/?pageIndex=${page.index + 1}">Next</a></li>
                    </ul>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Footer Area -->
    <c:import url="${ftr}" />
</div>
</body>
</html>
