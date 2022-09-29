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
            $('#alias').on('change', verifyAlias);
            <c:if test="${not empty userdata.id}">
            $('#pw').on('input', verifyPw);
            $('#pw_re').on('input', verifyPwRe);
            </c:if>
            $('#alias').focus();
        });

        function verifyAlias() {
            let objId = "#alias";
            let obj = $(objId);
            let objDesc = $(objId + "_desc");
            let result;

            if (obj.val() === "") {
                objDesc.text("닉네임 (별칭)을 입력해주세요.");
                return false;
            }

            $.ajax({
                url: '/my/verifyAlias',
                type: 'post',
                data: {alias: obj.val()},
                dataType: "json",
                async: false,
                success: function (r) {
                    result = r.result;
                },
                error: function () {
                    alert ('관리자에게 문의 바랍니다.');
                }
            });
            if (result.status != "ok") {
                objDesc.text(result.message);
                return false;
            }

            objDesc.text("");
            return true;
        }

        <c:if test="${not empty userdata.id}">
        function verifyPw() {
            let objId = "#pw";
            let obj = $(objId);
            let objDesc = $(objId + "_desc");

            objDesc.text("");
            return true;
        }

        function verifyPwRe() {
            let objId = "#pw_re";
            let obj = $(objId);
            let objDesc = $(objId + "_desc");

            if (obj.val() !== $('#pw').val()) {
                objDesc.text("암호가 일치하지 않습니다.");
                return false;
            }

            objDesc.text("");
            return true;
        }
        </c:if>

        function verifyEdit() {
            let verifyCode = true;

            (!verifyAlias()) ? verifyCode = false : null;
            <c:if test="${not empty userdata.id}">
            (!verifyPw()) ? verifyCode = false : null;
            (!verifyPwRe()) ? verifyCode = false : null;
            </c:if>

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
                        <h3 class="text-bold text-center mb-3">내 정보 수정하기</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col col-lg-6 mx-auto">
                        <form action="/my/edit" method="post" onsubmit="return verifyEdit()" autocomplete="off">
                            <div class="form-group">
                                <input type="text" class="form-control" id="alias" name="alias" placeholder="닉네임 (별명)" value="${alias}" />
                                <span class="text-danger mt-2" id="alias_desc"></span>
                            </div>
                            <c:if test="${not empty userdata.id}">
                                <div class="form-group">
                                    <input type="password" class="form-control" id="pw" name="pw" placeholder="암호" />
                                    <span class="text-danger mt-2" id="pw_desc"></span>
                                </div>
                                <div class="form-group">
                                    <input type="password" class="form-control" id="pw_re" placeholder="암호 확인" />
                                    <span class="text-danger mt-2" id="pw_re_desc"></span>
                                </div>
                            </c:if>
                            <div class="d-flex justify-content-center">
                                <button class="btn btn-info btn-sm mr-2" type="submit"><i class="fas fa-edit"></i> 수정하기</button>
                                <a class="btn btn-secondary btn-sm" href="/my"><i class="fas fa-arrow-circle-left"></i> 뒤로가기</a>
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
