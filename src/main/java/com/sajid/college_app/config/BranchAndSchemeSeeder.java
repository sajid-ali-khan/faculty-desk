package com.sajid.college_app.config;

import com.sajid.college_app.models.Scheme;
import com.sajid.college_app.models.SimpleBranch;
import com.sajid.college_app.repositories.SchemeRepository;
import com.sajid.college_app.repositories.SimpleBranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@Slf4j
public class BranchAndSchemeSeeder implements ApplicationRunner {
    private final SimpleBranchRepository simpleBranchRepository;
    private final SchemeRepository schemeRepository;

    public BranchAndSchemeSeeder(SimpleBranchRepository simpleBranchRepository, SchemeRepository schemeRepository) {
        this.simpleBranchRepository = simpleBranchRepository;
        this.schemeRepository = schemeRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.getOptionValues("seed-schemes-branches") == null){
            log.info("Branch seeder skipped.");
            return;
        }
        if (seedSchemes()){
            log.info("Schemes seeded successfully.");
        }else{
            log.info("Couldn't seed schemes.");
        }

        if (seedSimpleBranches()){
            log.info("Simple Branches seeded successfully.");
        }else{
            log.info("Couldn't seed simple branches.");
        }
    }

    private boolean seedSimpleBranches() {

        Map<Integer, String[]> branchNamesMap = new HashMap<>();
        branchNamesMap.put(1, new String[]{"CSE", "Computer Science & Engineering"});
        branchNamesMap.put(2, new String[]{"CIV", "Civil Engineering"});
        branchNamesMap.put(3, new String[]{"CST", "Computer Science & Technology"});
        branchNamesMap.put(4, new String[]{"ECE", "Electronics and Communication Engineering"});
        branchNamesMap.put(5, new String[]{"MEC", "Mechanical Engineering"});
        branchNamesMap.put(6, new String[]{"CSBS", "Computer Science & Business Systems"});
        branchNamesMap.put(7, new String[]{"EEE", "Electrical and Electronics Engineering"});
        branchNamesMap.put(8, new String[]{"CSD", "Data Science"});
        branchNamesMap.put(9, new String[]{"CSM", "Artificial Intelligence & Machine Learning"});
        List<SimpleBranch> simpleBranchList = new ArrayList<>();

        for (Map.Entry<Integer, String[]> entry: branchNamesMap.entrySet()){
            if (simpleBranchRepository.existsByBranchCode(entry.getKey())){
                continue;
            }
            SimpleBranch simpleBranch = new SimpleBranch();
            simpleBranch.setBranchCode(entry.getKey());
            simpleBranch.setShortForm(entry.getValue()[0]);
            simpleBranch.setFullForm(entry.getValue()[1]);

            simpleBranchList.add(simpleBranch);
        }

        try {
            simpleBranchRepository.saveAll(simpleBranchList);
            return true;
        } catch (Exception e){
            log.error("Branch Seeder: Something went wrong!, {}", e.getMessage());
            return false;
        }
    }

    private boolean seedSchemes(){
        String[] schemeCodes = new String[]{"20", "23", "20X", "20Y"};
        List<Scheme> schemes = new ArrayList<>();

        for (String schemeCode: schemeCodes){
            if (schemeRepository.existsBySchemeCode(schemeCode)){
                continue;
            }
            Scheme scheme = new Scheme(schemeCode);
            schemes.add(scheme);
        }
        try {
            schemeRepository.saveAll(schemes);
            return true;
        }catch (Exception e){
            log.error("Scheme Seeder: Something went wrong!, {}", e.getMessage());
            return false;
        }
    }
}
