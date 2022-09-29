<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>플레이리스트 :: 당신의 실행 목록을 기록하세요</title>
    <%@include file="/WEB-INF/views/static/modules/meta.jsp"%>
    <%@include file="/WEB-INF/views/static/modules/bootstrap.jsp"%>
    <%@include file="/WEB-INF/views/static/modules/common.jsp"%>
    <script type="text/javascript">
        function del(idx) {
            let d = confirm("정말로 삭제하시겠습니까?");
            if (d) {
                location.href = "/play/" + idx + "/delete";
            }
        }
    </script>
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
                        <h3 class="text-bold text-center mb-3">나의 플레이리스트</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-6 mx-auto">
                        <div class="d-flex justify-content-between">
                            <div class="d-flex">
                                <a class="btn btn-light-border" href="/" title="뒤로가기"><i class="fas fa-arrow-left"></i></a>
                            </div>
                            <div class="d-flex">
                                <a class="btn btn-light-border" href="/play/write" title="플레이리스트 추가"><i class="fas fa-plus"></i></a>
                            </div>
                        </div>
                        <hr/>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-6 mx-auto">
                        <c:choose>
                            <c:when test="${playList.size() > 0}">
                                <c:forEach items="${playList}" var="list">
                                    <div class="form-group d-flex justify-content-between">
                                        <a class="d-flex flex-fill pr-1 text-decoration-none" href="/play/${list.idx}">
                                            <div class="form-control">${list.title}</div>
                                        </a>
                                        <div class="d-flex">
                                            <a class="btn btn-light-border text-secondary mr-1" href="/play/${list.idx}/edit" title="수정하기"><i class="fas fa-cog"></i></a>
                                            <a class="btn btn-light-border text-danger" href="javascript: del('${list.idx}')" title="삭제하기"><i class="fas fa-trash"></i></a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center mb-3">
                                    <p>플레이리스트가 존재하지 않습니다.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-6 mx-auto">
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
                                <li class="page-item<c:if test="${page.index == 1}"> disabled</c:if>"><a class="page-link" href="/play/?pageIndex=${page.index - 1}">Prev</a></li>
                                <c:forEach begin="${pageBegin - 2}" end="${pageEnd - 2}" var="pageNum">
                                    <c:if test="${pageNum > 0 && pageNum <= page.size}">
                                        <li class="page-item<c:if test="${page.index == pageNum}"> active</c:if>"><a class="page-link" href="/play/?pageIndex=${pageNum}">${pageNum}</a></li>
                                    </c:if>
                                </c:forEach>
                                <li class="page-item<c:if test="${page.index == page.size}"> disabled</c:if>"><a class="page-link" href="/play/?pageIndex=${page.index + 1}">Next</a></li>
                            </ul>
                        </c:if>
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
