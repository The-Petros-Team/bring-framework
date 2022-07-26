package com.bobocode.petros.bring.scanner.mocks.scenario.privatemethod;

public class GoogleCloudService implements CloudService {

    @Override
    public void saveDataInCloud(String data) {
        System.out.printf("Saving %s in Google Cloud...%n", data);
    }
}
