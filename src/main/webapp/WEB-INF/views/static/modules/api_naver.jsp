<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${agent.matches('(pc|tablet|mac)')}">
    <!-- PC, Tablet, Mac 등 -->
    <script src="//sstatic-rmcnmv.pstatic.net/js/webplayer_4.18.40.all.26b7e2e3.min.js?"></script>
    <!-- 200803 이후 변경된 JS 파일 추가 -->
    <script src="//ssl.pstatic.net/img.tvcast/pc/js/eg/eg.1.3.0.pkgd.min.js"></script>
    <!--script src="//tv.naver.com/resources/release/js_v2/player/tvcast.pc.EmbedPlayer_20200703111014.js"></script-->
    <script src="/res/js/api/naver-tvcast-PrismPlayer_200803.js"></script>
    <script src="/res/js/api/naver-tvcast-BaseWrapPlayer_200803.js"></script>
    <script src="/res/js/api/naver-tvcast-EmbedPlayer_200803.js"></script>
    <script src="/res/js/api/naver-tvcast-LikeItController_200803.js"></script>
    <script src="/res/js/api_naver.js"></script>
</c:if>

<c:if test="${agent.matches('(mobile)')}">
    <!-- Mobile -->
    <!-- 모바일에선 자동 실행이 지원되지 않음 -->
    <script src="//sstatic-rmcnmv.pstatic.net/js/webplayer_4.18.40.mo.05500c08.min.js?2020080415"></script>
    <!-- 200803 이후 변경된 JS 파일 추가 -->
    <script src="//ssl.pstatic.net/img.tvcast/pc/js/eg/eg.1.3.0.pkgd.min.js"></script>
    <script src="/res/js/api/naver-tvcast-EmbedPlayer_200703.js"></script>
    <script src="/res/js/api_naver_mobile.js"></script>
</c:if>
<script type="text/javascript">
    let ghtEnv = {
        rmcSid: "2010"
        , apiDomain: "https://api.tv.naver.com"
        , serviceapiDomainRMC: "https://serviceapi.rmcnmv.naver.com"
        , techType: nhn.rmcnmv.TechType.AUTO
        , countryCode: "KR"
    };
    let ghtPlayerInfo = ${play.videoData};
    let prefix = '/play/${listId}/view/';
    let next = '${next.idx}';
</script>
<!-- 매 달 3일 JS 파일 업데이트로 고정 다운로드 -->