package com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Datas;

public class DataHistoryReport {

    private String bordingA;
    private String bordingB;
    private String bordingC;
    private String bordingD;
    private String images;
    private String inspectionRemarks;
    private String inspector;
    private String numberAdult;
    private String numberChildren;
    private String numberCrew;
    private String numberInfant;
    private String numberTotalPassenger;
    private String timeUploaded;
    private String vesselName;
    private String pushKey;
    private String Key;

    public DataHistoryReport() {
    }

    public DataHistoryReport(String bordingA, String bordingB, String bordingC, String bordingD, String images, String inspectionRemarks, String inspector, String numberAdult, String numberChildren, String numberCrew, String numberInfant, String numberTotalPassenger, String timeUploaded, String vesselName, String pushKey, String Key) {
        this.bordingA = bordingA;
        this.bordingB = bordingB;
        this.bordingC = bordingC;
        this.bordingD = bordingD;
        this.images = images;
        this.inspectionRemarks = inspectionRemarks;
        this.inspector = inspector;
        this.numberAdult = numberAdult;
        this.numberChildren = numberChildren;
        this.numberCrew = numberCrew;
        this.numberInfant = numberInfant;
        this.numberTotalPassenger = numberTotalPassenger;
        this.timeUploaded = timeUploaded;
        this.vesselName = vesselName;
        this.pushKey = pushKey;
        this.Key = Key;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getBordingA() {
        return bordingA;
    }

    public void setBordingA(String bordingA) {
        this.bordingA = bordingA;
    }

    public String getBordingB() {
        return bordingB;
    }

    public void setBordingB(String bordingB) {
        this.bordingB = bordingB;
    }

    public String getBordingC() {
        return bordingC;
    }

    public void setBordingC(String bordingC) {
        this.bordingC = bordingC;
    }

    public String getBordingD() {
        return bordingD;
    }

    public void setBordingD(String bordingD) {
        this.bordingD = bordingD;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getInspectionRemarks() {
        return inspectionRemarks;
    }

    public void setInspectionRemarks(String inspectionRemarks) {
        this.inspectionRemarks = inspectionRemarks;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getNumberAdult() {
        return numberAdult;
    }

    public void setNumberAdult(String numberAdult) {
        this.numberAdult = numberAdult;
    }

    public String getNumberChildren() {
        return numberChildren;
    }

    public void setNumberChildren(String numberChildren) {
        this.numberChildren = numberChildren;
    }

    public String getNumberCrew() {
        return numberCrew;
    }

    public void setNumberCrew(String numberCrew) {
        this.numberCrew = numberCrew;
    }

    public String getNumberInfant() {
        return numberInfant;
    }

    public void setNumberInfant(String numberInfant) {
        this.numberInfant = numberInfant;
    }

    public String getNumberTotalPassenger() {
        return numberTotalPassenger;
    }

    public void setNumberTotalPassenger(String numberTotalPassenger) {
        this.numberTotalPassenger = numberTotalPassenger;
    }

    public String getTimeUploaded() {
        return timeUploaded;
    }

    public void setTimeUploaded(String timeUploaded) {
        this.timeUploaded = timeUploaded;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }
}
