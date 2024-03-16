package org.dstu.bpm;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription("check-show")
public class CheckShowHandler implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        RestTemplate rt = new RestTemplate();
        Object[] response = rt.getForObject("http://localhost:8090/movie/list", Object[].class);
        Map<String, Object> vars = new LinkedHashMap<>();

        if (response != null && response.length > 0) {
            vars.put("showAvailable", true);
            vars.put("moviesList", response);
            externalTaskService.complete(externalTask, vars);
        } else {
            vars.put("showAvailable", false);
            externalTaskService.complete(externalTask);
        }
    }
}
