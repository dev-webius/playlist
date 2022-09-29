<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="//www.youtube.com/iframe_api"></script>
<script type="text/javascript">
    let player;
    function onYouTubeIframeAPIReady() {
        player = new YT.Player('player', {
            /*
            width: '640',
            height: '360',
            */
            videoId: '${play.videoId}',
            events: {
                'onReady': onPlayerReady,
                'onStateChange': onPlayerStateChange
            }
        });
    }
    let prefix = '/play/${listId}/view/';
    let next = '${next.idx}';
</script>
<script src="/res/js/api_youtube.js"></script>