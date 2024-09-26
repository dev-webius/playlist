window.addEventListener('load', () => {
    $('#subject').on('input', verifySubject);
    $('#content').on('input', verifyContent);
    ($('#id').val() == "") ? $('#id').focus() : $('#pw').focus();
});

function verifySubject() {
    let objId = "#subject";
    let obj = $(objId);
    let objDesc = $(objId + "_desc");

    if (obj.val() === "") {
        objDesc.text("제목을 입력해주세요.");
        return false;
    }

    objDesc.text("");
    return true;
}

function verifyContent() {
    let objId = "#content";
    let obj = $(objId);
    let objDesc = $(objId + "_desc");

    if (obj.val() === "") {
        objDesc.text("내용을 입력해주세요.");
        return false;
    }

    objDesc.text("");
    return true;
}

function verifyNotice() {
    let verifyCode = true;

    (!verifySubject()) ? verifyCode = false : null;
    (!verifyContent()) ? verifyCode = false : null;

    if (verifyCode)
        $('#submitBtn').attr('disabled', '');

    return verifyCode;
}