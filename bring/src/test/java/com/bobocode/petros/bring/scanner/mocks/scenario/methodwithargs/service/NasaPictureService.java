package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.service;

public class NasaPictureService implements PictureService {

    @Override
    public void showPicture(String pictureName) {
        System.out.printf("Displaying %s%n", pictureName);
    }
}
