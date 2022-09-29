package net.webius.playlist.sign;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignCheckUtil {
    private final SignServiceImpl signService;

    public SignCheckUtil(SignServiceImpl signService) {
        this.signService = signService;
    }

    List<String> EXCLUDE_ID;
    List<String> EXCLUDE_ALIAS;

    public Boolean verify(SignVO signVO) {
        // Null 검증
        if (signVO.getId() == null)
            return false;
        if (signVO.getPw() == null)
            return false;
        if (signVO.getAlias() == null)
            return false;

        // ID 예외 검증
        if (isExcludeId(signVO.getId()))
            return false;
        // 별칭 예외 검증
        if (isExludeAlias(signVO.getAlias()))
            return false;

        try {
            // ID 중복 검증
            if (signService.hasId(new SignVerifyVO(null, signVO.getId(), null)))
                return false;

            // 별칭 중복 검증
            if (signService.hasAlias(new SignVerifyVO(null, null, signVO.getAlias())))
                return false;
        } catch (SignException e) {
            e.printStackTrace();
        }

        return true;
    }

    public Boolean verify(SignEditVO signEditVO) {
        // Null 검증
        if (signEditVO.getPw() != null && signEditVO.getPw().equals(""))
        if (signEditVO.getAlias() == null)
            return false;

        // 별칭 예외 검증
        if (isExludeAlias(signEditVO.getAlias()))
            return false;

        try {
            // 별칭 중복 검증
            if (signService.hasAlias(new SignVerifyVO(signEditVO.getUid(), null, signEditVO.getAlias())))
                return false;
        } catch (SignException e) {
            e.printStackTrace();
        }

        return true;
    }

    public Boolean verifyApi(SignVO signVO) {
        // Null 검증
        if (signVO.getAlias() == null)
            return false;

        // 별칭 예외 검증
        if (isExludeAlias(signVO.getAlias()))
            return false;

        try {
            // 별칭 중복 검증
            if (signService.hasAlias(new SignVerifyVO(null, null, signVO.getAlias())))
                return false;
        } catch (SignException e) {
            e.printStackTrace();
        }

        return true;
    }

    public Boolean isExcludeId(String id) {
        return EXCLUDE_ID.contains(id);
    }

    public Boolean isExludeAlias(String alias) {
        return EXCLUDE_ALIAS.contains(alias);
    }
}
