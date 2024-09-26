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
        function del() {
            let d = confirm("정말로 삭제하시겠습니까?");
            if (d) {
                location.href = "/notice/${notice.nid}/delete";
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
                <h1 class="mb-2">${notice.subject}</h1>
                <div class="d-flex flex-column flex-lg-row justify-content-between">
                    <div class="d-flex">
                        <h6>작성자: ${notice.alias}</h6>
                    </div>
                    <div class="d-flex">
                        <h6>${notice.cdate}</h6>
                        <h6 class="ml-3">조회수: ${notice.view}</h6>
                    </div>
                </div>
                <hr/>
                <div class="mb-5">
                    ${notice.content}
                </div>
                <div class="d-flex justify-content-end">
                    <c:if test="${user.UID == '1'}">
                        <a class="btn btn-danger btn-sm mr-2" href="javascript: del()"><i class="fas fa-trash"></i> 삭제</a>
                        <a class="btn btn-info btn-sm mr-2" href="/notice/${notice.nid}/edit"><i class="fas fa-edit"></i> 수정</a>
                    </c:if>
                    <a class="btn btn-secondary btn-sm" href="/notice"><i class="fas fa-arrow-circle-left"></i> 목록</a>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer Area -->
    <c:import url="${ftr}" />
</div>
</body>
</html>
