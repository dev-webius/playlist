<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>플레이리스트 :: 당신의 실행 목록을 기록하세요</title>
    <%@include file="/WEB-INF/views/static/modules/meta.jsp"%>
    <%@include file="/WEB-INF/views/static/modules/bootstrap.jsp"%>
    <%@include file="/WEB-INF/views/static/modules/common.jsp"%>
    <script src="/res/js/notice.js"></script>
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
                        <h3 class="text-bold text-center mb-3">게시글 작성</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-6 mx-auto">
                        <form action="/notice/write" method="post" onsubmit="return verifyNotice()" autocomplete="off">
                            <div class="form-group">
                                <p>제목</p>
                                <input class="form-control" type="text" id="subject" name="subject" />
                                <span class="text-danger mt-2" id="subject_desc"></span>
                            </div>
                            <div class="form-group">
                                <p>내용</p>
                                <textarea class="form-control" type="text" id="content" name="content" rows="5"></textarea>
                                <span class="text-danger mt-2" id="content_desc"></span>
                            </div>
                            <div class="d-flex justify-content-end">
                                <a class="btn btn-secondary btn-sm mr-2" href="/notice"><i class="fas fa-arrow-circle-left"></i> 목록으로</a>
                                <button id="submitBtn" class="btn btn-playlist btn-sm" type="submit"><i class="fas fa-pen"></i> 작성하기</button>
                            </div>
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
