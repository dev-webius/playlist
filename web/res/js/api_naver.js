window.addEventListener('load', function() {
    EmbedPlayer.init(ghtEnv, ghtPlayerInfo);
    let players = BaseWrapPlayer._player;

    //let m_adskip;
    let m_autostart = setInterval(() => {
        if (players.currentTime == 0) {
            players.play();
        } else {
            clearInterval(m_autostart);
            m_autostart = null;
            /*
            200803 이후 광고 뜨지 않음
            m_adskip = setInterval(() => {
                if (players == "ad") {
                    setTimeout(() => {
                        players.skipAd();
                    }, 5100);
                }
            }, 1000);
            */
        }
    }, 500);

    players.addEventListener('ended', () => { // stop event -> ended (200703 -> 200803 규격이 완전히 달라짐)
        /*
        clearInterval(m_adskip);
        m_adskip = null;
        */
        if (next !== '') {
            // 다음 영상이 있는 경우
            location.href = prefix + next;
        }
    });
});