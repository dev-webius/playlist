package net.webius.playlist.main;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.login.UserVO;
import net.webius.playlist.play.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class MainController {
    private final MainServiceImpl mainService;
    private final PlayServiceImpl playService;
    private final PlayNodeServiceImpl playNodeService;

    public MainController(MainServiceImpl mainService, PlayServiceImpl playService, PlayNodeServiceImpl playNodeService) {
        this.mainService = mainService;
        this.playService = playService;
        this.playNodeService = playNodeService;
    }

    @GetMapping("/")
    public String main(HttpServletRequest request,
                       ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        if (userVO == null) {
            // 세션이 없는 경우
            return "/main/welcome";
        }

        try {
            MainFeedVO listFeed = mainService.getList(userVO.getUID());
            if (listFeed != null) {
                modelMap.addAttribute("list", playService.list(listFeed.getPid()));
            }

            List<MainFeedVO> nodeFeed = mainService.getNode(userVO.getUID());
            if (nodeFeed.size() > 0) {
                List<PlayVO> play = new ArrayList<PlayVO>();
                for (MainFeedVO feed : nodeFeed) {
                    play.add(new PlayVO(
                            playService.list(feed.getPid()),
                            playNodeService.node(feed.getPbid())
                    ));
                }
                modelMap.addAttribute("play", play);
            }
        } catch (MainException e) {
            e.printStackTrace();
        } catch (PlayException e) {
            e.printStackTrace();
        }
        // 세션이 있는 경우
        return "/main/main";
    }
}
