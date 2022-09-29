package net.webius.playlist.sign;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.login.LoginApiVO;
import net.webius.playlist.login.LoginException;
import net.webius.playlist.login.LoginServiceImpl;
import net.webius.playlist.login.UserVO;
import net.webius.playlist.util.MVCUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Controller
public class SignController {
    private final SignServiceImpl signService;
    private final LoginServiceImpl loginService;
    private final SignCheckUtil signCheckUtil;

    public SignController(SignServiceImpl signService, LoginServiceImpl loginService, SignCheckUtil signCheckUtil) {
        this.signService = signService;
        this.loginService = loginService;
        this.signCheckUtil = signCheckUtil;
    }

    @GetMapping("/sign")
    public String sign() {
        return "/sign/sign";
    }

    @PostMapping("/sign")
    public String sign(@RequestParam("id") String id,
                       @RequestParam("pw") String pw,
                       @RequestParam("alias") String alias,
                       ModelMap modelMap) {

        SignVO signVO = new SignVO(id, pw, alias);
        if (signCheckUtil.verify(signVO)) {
            // 가입 데이터 검증 후 진행
            try {
                // 회원가입 성공 시
                if (signService.sign(signVO)) {
                    return "/sign/welcome";
                }
            } catch (SignException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 회원가입 실패 시
        modelMap.addAttribute("errmsg", "잘못된 접근입니다.");
        return "/sign/signerr";
    }

    @GetMapping("/sign/{apiName}")
    public String signApi(@PathVariable("apiName") String apiName, HttpServletRequest request, ModelMap modelMap) {
        if ((!apiName.equals("google") && !apiName.equals("naver") && !apiName.equals("kakao")) ||
                request.getSession().getAttribute("apiData") == null) {
            modelMap.addAttribute("errmsg", "잘못된 접근입니다.");
            return "/sign/signerr";
        }
        return "/sign/signApi";
    }

    @PostMapping("/sign/{apiName}")
    public String signApi(@PathVariable("apiName") String apiName,
                          @RequestParam("alias") String alias,
                          HttpServletRequest request,
                          ModelMap modelMap) {

        // API Name 검증
        if (!apiName.equals("google") && !apiName.equals("naver") && !apiName.equals("kakao")) {
            modelMap.addAttribute("errmsg", "잘못된 접근입니다.");
            return "/sign/signerr";
        }

        // API 로그인 세션이 없는 경우, 에러 페이지 이동
        LoginApiVO apiObject = (LoginApiVO) request.getSession().getAttribute("apiData");
        if (apiObject == null) {
            modelMap.addAttribute("errmsg", "계정 정보를 확인할 수 없습니다.");
            return "/sign/signerr";
        }

        SignVO signVO = new SignVO(apiObject.getId(), alias);
        if (signCheckUtil.verifyApi(signVO)) {
            // 가입 데이터 검증 후 진행
            try {
                Boolean signok;
                if (apiName.equals("google"))
                    signok = signService.signGoogle(signVO);
                else if (apiName.equals("naver"))
                    signok = signService.signNaver(signVO);
                else
                    signok = signService.signKakao(signVO); // apiName.equals("kakao")

                // 회원가입 성공 시
                if (signok) {
                    return "/sign/welcome";
                }
            } catch (SignException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 회원가입 실패 시
        modelMap.addAttribute("errmsg", "잘못된 접근입니다.");
        return "/sign/signerr";
    }

    @GetMapping("/my")
    public String my(HttpServletRequest request, ModelMap modelMap) {
        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        modelMap.addAttribute("alias", userVO.getALIAS());
        try {
            modelMap.addAttribute("userdata", loginService.getUser(userVO.getUID()));
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return "/main/my";
    }

    @GetMapping("/my/edit")
    public String myEdit(HttpServletRequest request, ModelMap modelMap) {
        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        modelMap.addAttribute("alias", userVO.getALIAS());
        try {
            modelMap.addAttribute("userdata", loginService.getUser(userVO.getUID()));
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return "/main/myedit";
    }

    @PostMapping("/my/edit")
    public ModelAndView myEdit(@RequestParam("alias") String alias,
                               @RequestParam(value = "pw", required = false) String pw,
                               HttpServletRequest request,
                               ModelAndView modelAndView) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        SignEditVO signEditVO;
        if (pw == null)
            signEditVO = new SignEditVO(userVO.getUID(), alias);
        else {
            if (pw.equals(""))
                signEditVO = new SignEditVO(userVO.getUID(), alias);
            else
                signEditVO = new SignEditVO(userVO.getUID(), pw, alias);
        }

        if (signCheckUtil.verify(signEditVO)) {
            // 수정 데이터 검증 후 진행
            try {
                // 내 정보 수정 성공 시
                if (signService.edit(signEditVO)) {
                    request.getSession().setAttribute("user", new UserVO(signEditVO.getUid(), signEditVO.getAlias()));
                    modelAndView.setView(MVCUtil.redirect("/my/"));
                    return modelAndView;
                }
            } catch (SignException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 수정 실패 시
        modelAndView.addObject("error", "오류가 발생했습니다.");
        modelAndView.setViewName("/main/alert");
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(value = "/my/verifyAlias")
    public String verifyAlias(@RequestParam("alias") String alias, HttpServletRequest request) {
        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<String, Object>();
        SignAlertVO signAlertVO;

        try {
            if (signCheckUtil.isExludeAlias(alias))
                signAlertVO = new SignAlertVO("error", "사용할 수 없는 닉네임 입니다.");
            else if (signService.hasAlias(new SignVerifyVO(userVO.getUID(), null, alias)))
                signAlertVO = new SignAlertVO("error", "중복된 닉네임이 존재합니다.");
            else
                signAlertVO = new SignAlertVO("ok");

            map.put("result", signAlertVO);
            return mapper.writeValueAsString(map);
        } catch (SignException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "/sign/verifyId")
    public String verifyId(@RequestParam("id") String id) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<String, Object>();
        SignAlertVO signAlertVO;

        try {
            if (signCheckUtil.isExcludeId(id))
                signAlertVO = new SignAlertVO("error", "사용할 수 없는 아이디 입니다.");
            else if (signService.hasId(new SignVerifyVO("-1", id, null)))
                signAlertVO = new SignAlertVO("error", "중복된 아이디가 존재합니다.");
            else
                signAlertVO = new SignAlertVO("ok");

            map.put("result", signAlertVO);
            return mapper.writeValueAsString(map);
        } catch (SignException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "/sign/verifyAlias")
    public String verifyAlias(@RequestParam("alias") String alias) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<String, Object>();
        SignAlertVO signAlertVO;

        try {
            if (signCheckUtil.isExludeAlias(alias))
                signAlertVO = new SignAlertVO("error", "사용할 수 없는 닉네임 입니다.");
            else if (signService.hasAlias(new SignVerifyVO("-1", null, alias)))
                signAlertVO = new SignAlertVO("error", "중복된 닉네임이 존재합니다.");
            else
                signAlertVO = new SignAlertVO("ok");

            map.put("result", signAlertVO);
            return mapper.writeValueAsString(map);
        } catch (SignException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
