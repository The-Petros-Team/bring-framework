package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.service;

import com.bobocode.petros.bring.annotation.Service;

@Service
public class PictureAnalyzerService implements AnalyzerService {

    @Override
    public void analyze(String file) {
        System.out.printf("Analyzing file '%s'...", file);
    }
}
