window.addEventListener('load', () => {
    let m_autostart = setInterval(() => {
        if ($('#player')[0].currentTime == 0) {
            $('#player')[0].play();
        } else {
            clearInterval(m_autostart);
            m_autostart = null;
        }
    }, 500);

    /*
    $('#player').on('play', () => {
        console.log('play test');
    });

    $('#player').on('playing', () => {
        console.log('playing test');
    });

    $('#player').on('pause', () => {
        console.log('pause test');
    });
    */

    $('#player').on('ended', () => {
        if (next !== '') {
            // 다음 영상이 있는 경우
            location.href = prefix + next;
        }
    });
});