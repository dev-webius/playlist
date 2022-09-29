package net.webius.playlist.util;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class PlatformUtil {
    private final HostConnectionUtil hostConnectionUtil;

    public PlatformUtil(HostConnectionUtil hostConnectionUtil) {
        this.hostConnectionUtil = hostConnectionUtil;
    }

    List<String> YOUTUBE_PATTERN;
    List<String> NAVER_PATTERN;
    List<String> KAKAO_PATTERN;

    public String getPlatform(String url) {
        if (isPlatform(YOUTUBE_PATTERN, url))
            return "Youtube";
        if (isPlatform(NAVER_PATTERN, url))
            return "Naver";
        if (isPlatform(KAKAO_PATTERN, url))
            return "Kakao";
        return "Unknown";
    }

    public int getPlatformCode(String platform) {
        if (platform.equals("Youtube"))
            return 1;
        if (platform.equals("Naver"))
            return 2;
        if (platform.equals("Kakao"))
            return 3;
        return 0;
    }

    public String getPlatformName(int code) {
        if (code == 1)
            return "Youtube";
        if (code == 2)
            return "Naver";
        if (code == 3)
            return "Kakao";
        return "Unknown";
    }

    public String getVideoId(String url) {
        return getValue(getPattern(getPlatform(url)), url, 1);
    }

    public String getVideoId(String platform, String url) {
        return getValue(getPattern(platform), url, 1);
    }

    public String getThumbnail(String platform, String url) {
        try {
            if (platform.equals("Youtube")) {
                return "https://i.ytimg.com/vi/" + getVideoId(url) + "/maxresdefault.jpg";
            }

            if (platform.equals("Naver") || platform.equals("Kakao")) {
                return hostConnectionUtil.getMetaValue(hostConnectionUtil.get(url).parse(), "og:image");
            }
        } catch (HostConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getThumbnail(Document document, String platform) {
        if (platform.equals("Naver") || platform.equals("Kakao")) {
            return hostConnectionUtil.getMetaValue(document, "og:image");
        }
        return null;
    }

    public String getThumbnailVideoId(String platform, String videoId) {
        try {
            if (platform.equals("Youtube")) {
                return "https://i.ytimg.com/vi/" + videoId + "/mqdefault.jpg";
            }

            if (platform.equals("Naver")) {
                return getThumbnail(hostConnectionUtil.get("https://tv.naver.com/v/" + videoId).parse(), platform);
            }

            if (platform.equals("Kakao")) {
                return getThumbnail(hostConnectionUtil.get("https://tv.kakao.com/v/" + videoId).parse(), platform);
            }
        } catch (HostConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getYoutubeUrl(String videoId, int type) {
        if (type == 1)
            return "https://www.youtube.com/watch?v=" + videoId;
        return null;
    }

    public String getNaverUrl(String videoId, int type) {
        if (type == 1)
            return "https://tv.naver.com/embed/" + videoId;
        if (type == 2)
            return "https://tv.naver.com/v/" + videoId;
        if (type == 3)
            return "https://m.tv.naver.com/embed/" + videoId;
        return null;
    }

    public String getKakaoUrl(String videoId, int type) {
        if (type == 1)
            return "https://play-tv.kakao.com/katz/v1/ft/cliplink/" + videoId + "/readyNplay?player=monet_html5&profile=HIGH&service=und_player&dteType=PC";
        if (type == 2)
            return "https://tv.kakao.com/v/" + videoId;
        return null;
    }

    private List<String> getPattern(String platform) {
        if (platform.equals("Youtube"))
            return YOUTUBE_PATTERN;

        if (platform.equals("Naver"))
            return NAVER_PATTERN;

        if (platform.equals("Kakao"))
            return KAKAO_PATTERN;

        return null;
    }

    private String getValue(List<String> PATTERN, String url, int groupIndex) {
        Pattern pattern;
        Matcher matcher;
        for (String regexp : PATTERN) {
            pattern = Pattern.compile(regexp);
            matcher = pattern.matcher(url);
            if (matcher.find())
                return matcher.group(groupIndex);
        }
        return null;
    }

    private Boolean isPlatform (List<String> platform, String url) {
        Pattern pattern;
        Matcher matcher;
        for (String regexp : platform) {
            pattern = Pattern.compile(regexp);
            matcher = pattern.matcher(url);
            if (matcher.find())
                return true;
        }
        return false;
    }
}
