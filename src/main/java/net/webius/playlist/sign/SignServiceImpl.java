package net.webius.playlist.sign;

import org.springframework.stereotype.Service;

@Service("signService")
public class SignServiceImpl implements SignService {
    private final SignDAOImpl signDAO;

    public SignServiceImpl(SignDAOImpl signDAO) {
        this.signDAO = signDAO;
    }

    public Boolean sign(SignVO signVO) throws SignException {
        return (signDAO.sign(signVO) & signDAO.signAuth(signVO)) > 0;
    }

    public Boolean signGoogle(SignVO signVO) throws SignException {
        return (signDAO.sign(signVO) & signDAO.signGoogle(signVO)) > 0;
    }

    public Boolean signNaver(SignVO signVO) throws SignException {
        return (signDAO.sign(signVO) & signDAO.signNaver(signVO)) > 0;
    }

    public Boolean signKakao(SignVO signVO) throws SignException {
        return (signDAO.sign(signVO) & signDAO.signKakao(signVO)) > 0;
    }

    public Boolean edit(SignEditVO signEditVO) throws SignException {
        if (signEditVO.getPw() == null)
            return (signDAO.edit(signEditVO) > 0);
        else
            return (signDAO.edit(signEditVO) & signDAO.editAuth(signEditVO)) > 0;
    }

    public Boolean hasId(SignVerifyVO verifyVO) throws SignException {
        return signDAO.getId(verifyVO) > 0;
    }

    public Boolean hasAlias(SignVerifyVO verifyVO) throws SignException {
        return signDAO.getAlias(verifyVO) > 0;
    }
}
