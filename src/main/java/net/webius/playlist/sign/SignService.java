package net.webius.playlist.sign;

public interface SignService {
    public Boolean sign(SignVO signVO) throws SignException;
    public Boolean signGoogle(SignVO signVO) throws SignException;
    public Boolean signNaver(SignVO signVO) throws SignException;
    public Boolean signKakao(SignVO signVO) throws SignException;
    public Boolean edit(SignEditVO signEditVO) throws SignException;
    public Boolean hasId(SignVerifyVO verifyVO) throws SignException;
    public Boolean hasAlias(SignVerifyVO verifyVO) throws SignException;
}
