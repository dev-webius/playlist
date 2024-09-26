window.addEventListener('load', () => {
    $('#id').on('input', verifyId);
    $('#pw').on('input', verifyPw);
    ($('#id').val() == "") ? $('#id').focus() : $('#pw').focus();
});

function verifyId() {
    let objId = "#id";
    let obj = $(objId);
    let objDesc = $(objId + "_desc");

    if (obj.val() === "") {
        objDesc.text("아이디를 입력해주세요.");
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

    objDesc.text("");
    return true;
}

function verifyLogin() {
    let verifyCode = true;

    (!verifyId()) ? verifyCode = false : null;
    (!verifyPw()) ? verifyCode = false : null;

    if (verifyCode)
        $('#submitBtn').attr('disabled', '');

    return verifyCode;
}