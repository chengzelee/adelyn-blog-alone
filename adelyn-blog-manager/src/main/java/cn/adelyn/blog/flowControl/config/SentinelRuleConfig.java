package cn.adelyn.blog.flowControl.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class SentinelRuleConfig implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        List<FlowRule> flowRuleList = new ArrayList<>();
        // 创建限流规则
        FlowRule login = new FlowRule("login");
        // 设置每秒允许访问次数
        login.setCount(1);
        // 设置限流模式为QPS
        login.setGrade(RuleConstant.FLOW_GRADE_QPS);

        FlowRule tokenRefresh = new FlowRule("token_refresh");
        tokenRefresh.setCount(1);
        tokenRefresh.setGrade(RuleConstant.FLOW_GRADE_QPS);

        FlowRule picGet = new FlowRule("pic_get");
        picGet.setCount(50);
        picGet.setGrade(RuleConstant.FLOW_GRADE_QPS);

        flowRuleList.add(login);
        flowRuleList.add(tokenRefresh);
        flowRuleList.add(picGet);

        // 将规则添加到FlowRuleManager
        FlowRuleManager.loadRules(flowRuleList);

        log.info("init sentinel rule success");
    }
}
