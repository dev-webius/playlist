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
                location.href = "/play/${listId}/delete/" + idx;
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
            <div class="container-lg px-0 px-lg-3" style="margin-top: 3rem; margin-bottom: 5rem">
                <h3 class="text-bold text-center mb-3">${playTitle}</h3>
                <div class="d-flex justify-content-between mb-3 px-3 px-lg-0">
                    <div class="d-flex">
                        <a class="btn btn-light-border" href="/play" title="뒤로가기"><i class="fas fa-arrow-left"></i></a>
                    </div>
                    <div class="d-flex">
                        <a class="btn btn-light-border mr-1" href="/play/${listId}/youtube" title="유튜브 재생"><i class="btn-icon btn-youtube-icon"></i></a>
                        <a class="btn btn-light-border mr-1" href="/play/${listId}/naver" title="네이버TV 재생"><i class="btn-icon btn-naver-icon"></i></a>
                        <a class="btn btn-light-border mr-3" href="/play/${listId}/kakao" title="카카오TV 재생"><i class="btn-icon btn-kakao-icon"></i></a>
                        <a class="btn btn-light-border mr-1" href="/play/${listId}/surple" title="셔플 재생"><i class="fas fa-random"></i></a>
                        <a class="btn btn-light-border" href="/play/${listId}/write" title="URL 추가"><i class="fas fa-plus"></i></a>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${playNode.size() > 0}">
                        <ul class="table table-hover" data-role="playlist">
                            <c:forEach items="${playNode}" var="node">
                                <li class="mb-3 mb-lg-0">
                                    <div class="d-flex justify-content-between flex-column flex-lg-row">
                                        <a class="d-flex flex-column flex-lg-row flex-fill pr-lg-1" href="/play/${listId}/view/${node.idx}">
                                            <div class="mr-lg-3 mb-2 mb-lg-0" data-column="thumbnail">
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
                                            <div class="pr-lg-3 py-lg-3 px-3 px-lg-0" data-column="detail">
                                                <h5>${node.name}</h5>
                                                <p class="d-none d-lg-block">${node.url}</p>
                                            </div>
                                        </a>
                                        <div class="d-flex flex-row flex-lg-column justify-content-end justify-content-lg-center mr-2 mb-3 mb-lg-0">
                                            <a class="btn btn-light-border text-secondary mr-1 mr-lg-0 mb-lg-1" href="/play/${listId}/edit/${node.idx}" title="수정하기"><i class="fas fa-cog"></i></a>
                                            <a class="btn btn-light-border text-danger" href="javascript: del('${node.idx}')" title="삭제하기"><i class="fas fa-trash"></i></a>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <hr/>
                        <div class="text-center mb-3">
                            <p>플레이리스트 영상이 존재하지 않습니다.</p>
                        </div>
                    </c:otherwise>
                </c:choose>

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
                        <li class="page-item<c:if test="${page.index == 1}"> disabled</c:if>"><a class="page-link" href="/play/${listId}/?pageIndex=${page.index - 1}">Prev</a></li>
                        <c:forEach begin="${pageBegin - 2}" end="${pageEnd - 2}" var="pageNum">
                            <c:if test="${pageNum > 0 && pageNum <= page.size}">
                                <li class="page-item<c:if test="${page.index == pageNum}"> active</c:if>"><a class="page-link" href="/play/${listId}/?pageIndex=${pageNum}">${pageNum}</a></li>
                            </c:if>
                        </c:forEach>
                        <li class="page-item<c:if test="${page.index == page.size}"> disabled</c:if>"><a class="page-link" href="/play/${listId}/?pageIndex=${page.index + 1}">Next</a></li>
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