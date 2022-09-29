function onPlayerReady(e) {
    e.target.playVideo();
}

let player_done = false;
function onPlayerStateChange(e) {
    if (e.data === YT.PlayerState.ENDED) {
        // 영상이 종료되면 다음 영상으로 이동
        if (next !== '') {
            // 다음 영상이 있는 경우
            location.href = prefix + next;
        }
    }
    /*
    e.data
   -1       init
    0       end
    1       playing
    2       stop
    3       ready

   -1 – 시작되지 않음
    0 – 종료
    1 – 재생 중
    2 – 일시중지
    3 – 버퍼링
    5 – 동영상 신호

    YT.PlayerState.ENDED
    YT.PlayerState.PLAYING
    YT.PlayerState.PAUSED
    YT.PlayerState.BUFFERING
    YT.PlayerState.CUED
    */
}

function stopVideo() {
    player.stopVideo();
}