package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static java.lang.Thread.sleep;

/**
 * Created by Hunseong on 2022/03/23
 */
@RequiredArgsConstructor
@Controller
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        // 동시다발적인 HTTP request에 대한 타이밍 테스트를 위해 추가
        sleep(1000);

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}
