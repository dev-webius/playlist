package net.webius.playlist.login;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.util.MVCUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LoginController {
    private final LoginServiceImpl loginService;

    public LoginController(LoginServiceImpl loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "url", defaultValue = "/") String url,
                        ModelMap modelMap) {

        modelMap.addAttribute("url", url);
        return "/login/login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("id") String id,
                              @RequestParam("pw") String pw,
                              @RequestParam(value = "url", defaultValue = "/") String url,
                              HttpServletRequest request,
                              ModelAndView modelAndView) {

        LoginVO loginVO = new LoginVO(id, pw);
        try {
            UserVO userVO = loginService.doLogin(loginVO);

            // 로그인 성공 시
            if (userVO != null) {
                modelAndView.setView(MVCUtil.redirect(url));

                HttpSession session = request.getSession();
                session.invalidate(); // 새로운 세션 발급
                session = request.getSession();
                session.setAttribute("user", userVO);
            }

            // 로그인 실패 시
            else {
                modelAndView.addObject("errmsg", "아이디와 비밀번호가 맞지 않습니다.");
                modelAndView.setViewName("/login/loginerr");
            }
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelAndView;
    }

    @GetMapping("/login/apiError")
    public String loginApiError(@RequestParam("code") String code,
                                @RequestParam("errmsg") String msg,
                                ModelMap modelMap) {

        LoginApiErrorVO loginApiError = new LoginApiErrorVO(code, msg);
        modelMap.addAttribute("error", loginApiError);
        return "/login/loginApiError";
    }

    @GetMapping("/logout")
    public ModelAndView logout(@RequestParam(value = "rturl", defaultValue = "/") String url,
                               HttpServletRequest request,
                               ModelAndView modelAndView) {

        request.getSession().invalidate();
        modelAndView.setView(MVCUtil.redirect(url));
        return modelAndView;
    }
}
