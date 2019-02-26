package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas;

public class DataReportsStats {


    private String Date;
    private String Month;
    private String Year;
    private String VesselTypeReports;


    public DataReportsStats(){
    }


    public DataReportsStats(String Date, String Month, String Year, String VesselTypeReports){

        this.Date = Date;
        this.Month = Month;
        this.Year = Year;
        this.VesselTypeReports = VesselTypeReports;

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getVesselTypeReports() {
        return VesselTypeReports;
    }

    public void setVesselTypeReports(String vesselTypeReports) {
        VesselTypeReports = vesselTypeReports;
    }
}
