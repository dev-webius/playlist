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
        window.addEventListener('load', () => {
            $('#name').on('change', verifyUrl);
            $('#title').focus();
        });

        function verifyUrl() {
            let objId = "#url";
            let obj = $(objId);
            let objDesc = $(objId + "_desc");

            if (obj.val() === "") {
                objDesc.text("Url을 입력해주세요.");
                return false;
            }

            objDesc.text("");
            return true;
        }

        function verifyWrite() {
            let verifyCode = true;

            (!verifyUrl()) ? verifyCode = false : null;

            if (verifyCode)
                $('#submitBtn').attr('disabled', '');

            return verifyCode;
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
                <div class="row mb-3">
                    <div class="col col-lg-6 mx-auto">
                        <h3 class="text-bold text-center mb-3">새 영상 추가하기</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-6 mx-auto">
                        <form action="/play/${listId}/write" method="post" onsubmit="return verifyWrite()" autocomplete="off">
                            <div class="form-group">
                                <input class="form-control" type="text" id="name" name="name" placeholder="표시될 제목을 입력해주세요." value="${node.name}" />
                            </div>
                            <div class="form-group">
                                <input class="form-control" type="text" id="url" name="url" placeholder="http:// 또는 https://" value="${node.url}" />
                                <span class="text-danger mt-2" id="url_desc"></span>
                            </div>
                            <div class="d-flex justify-content-center">
                                <a class="btn btn-secondary btn-sm mr-2" href="/play/${listId}"><i class="fas fa-arrow-circle-left"></i> 목록으로</a>
                                <button id="submitBtn" class="btn btn-playlist btn-sm" type="submit"><i class="fas fa-pen"></i> 추가하기</button>
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
