package net.webius.playlist.sign;

public interface SignDAO {
    public int sign(SignVO signVO) throws SignException;
    public int signAuth(SignVO signVO) throws SignException;
    public int signKakao(SignVO signVO) throws SignException;
    public int signNaver(SignVO signVO) throws SignException;
    public int signGoogle(SignVO signVO) throws SignException;
    public int edit(SignEditVO signEditVO) throws SignException;
    public int editAuth(SignEditVO signEditVO) throws SignException;
    public int getId(SignVerifyVO verifyVO) throws SignException;
    public int getAlias(SignVerifyVO verifyVO) throws SignException;
}
