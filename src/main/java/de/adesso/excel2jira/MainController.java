package de.adesso.excel2jira;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MainController implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.containsOption("--file")){
            String filename = args.getOptionValues("--file").get(0);
            //TODO: List<Issue> issues = mapper.map("filename");
        }
    }
}
