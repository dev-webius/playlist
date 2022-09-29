let embedPlayer;
$(window).on('load', () => {
    EmbedPlayer.init(ghtEnv, ghtPlayerInfo);
    let players = EmbedPlayer.rmcPlayer;

    let m_adskip;
    let m_autostart = setInterval(() => {
        if (players.currentTime() == 0) {
            players.play();
        } else {
            clearInterval(m_autostart);
            m_autostart = null;
            m_adskip = setInterval(() => {
                if (players.getPlayingVideoType() == "ad") {
                    setTimeout(() => {
                        players.skipAd();
                    }, 1000);
                }
            }, 1000);
        }
    }, 500);

    players.on('stop', () => {
        clearInterval(m_adskip);
        m_adskip = null;
        if (next !== '') {
            // 다음 영상이 있는 경우
            location.href = prefix + next;
        }
    });
})
