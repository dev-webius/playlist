window.addEventListener('load', () => {
    $('#id').on('change', verifyId);
    $('#pw').on('input', verifyPw);
    $('#pw_re').on('input', verifyPwRe);
    $('#alias').on('change', verifyAlias);
    ($('#id').val() === "") ? $('#id').focus() : $('#pw').focus();
});

function verifyId() {
    let objId = "#id";
    let obj = $(objId);
    let objDesc = $(objId + "_desc");
    let result = null;

    if (obj.val() === "") {
        objDesc.text("아이디를 입력해주세요.");
        return false;
    }

    $.ajax({
        url: '/sign/verifyId',
        type: 'post',
        data: {id: obj.val()},
        dataType: "json",
        async: false,
        success: function (r) {
            result = r.result;
        },
        error: function () {
            alert ('관리자에게 문의 바랍니다.');
        }
    });
    if (result.status !== "ok") {
        objDesc.text(result.message);
        return false;
    }

    objDesc.text("");
    return true;
}

function verifyPw() {
    let objId = "#pw";
    let obj = $(objId);
    let objDesc = $(objId + "_desc");

    if (obj.val() === "") {
        objDesc.text("암호를 입력해주세요.");
        return false;
    }

    //let regex = /[a-zA-Z0-9]/;
    //console.log(RegExp(obj.val()).test(regex));

    objDesc.text("");
    return true;
}

function verifyPwRe() {
    let objId = "#pw_re";
    let obj = $(objId);
    let objDesc = $(objId + "_desc");

    if (obj.val() === "") {
        objDesc.text("암호를 다시 한번 확인해주세요.");
        return false;
    }

    if (obj.val() !== $('#pw').val()) {
        objDesc.text("암호가 일치하지 않습니다.");
        return false;
    }

    objDesc.text("");
    return true;
}

function verifyAlias() {
    let objId = "#alias";
    let obj = $(objId);
    let objDesc = $(objId + "_desc");
    let result = null;

    if (obj.val() === "") {
        objDesc.text("닉네임 (별칭)을 입력해주세요.");
        return false;
    }

    $.ajax({
        url: '/sign/verifyAlias',
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
    if (result.status !== "ok") {
        objDesc.text(result.message);
        return false;
    }

    objDesc.text("");
    return true;
}

function verifySign() {
    let verifyCode = true;

    (!verifyId()) ? verifyCode = false : null;
    (!verifyPw()) ? verifyCode = false : null;
    (!verifyPwRe()) ? verifyCode = false : null;
    (!verifyAlias()) ? verifyCode = false : null;

    if (verifyCode)
        $('#submitBtn').attr('disabled', '');

    return verifyCode;
}