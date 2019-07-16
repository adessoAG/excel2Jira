package de.adesso.excel2jira;

import de.adesso.excel2jira.excel.ExcelMapper;
import de.adesso.excel2jira.excel.domain.Issue;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainController implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //if(args.containsOption("--file")){
            //String filename = args.getOptionValues("--file").get(0);

            String path = "C:\\Users\\kkrause\\Desktop\\issues.xlsx";

            ExcelMapper mapper = new ExcelMapper();
            List<Issue> issues = mapper.map(path);
            //TODO: List<Issue> issues = mapper.map("filename");
        //}
    }
}
