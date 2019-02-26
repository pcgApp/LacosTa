package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas;

public class DataVesselInfo {

    private String Vessel_Name;
    private String Vessel_Type;
    private String Vessel_Desc;
    private String VesselPassengerCapacity;
    private String VesselNumberOfCrew;

    public DataVesselInfo(){

    }

    public DataVesselInfo(String Vessel_Name, String Vessel_Type, String Vessel_Desc,
                          String VesselPassengerCapacity, String VesselNumberOfCrew){
        this.Vessel_Name = Vessel_Name;
        this.Vessel_Type = Vessel_Type;
        this.Vessel_Desc = Vessel_Desc;
        this.VesselPassengerCapacity = VesselPassengerCapacity;
        this.VesselNumberOfCrew = VesselNumberOfCrew;
    }

    public String getVessel_Name() {
        return Vessel_Name;
    }

    public void setVessel_Name(String vessel_Name) {
        Vessel_Name = vessel_Name;
    }

    public String getVessel_Type() {
        return Vessel_Type;
    }

    public void setVessel_Type(String vessel_Type) {
        Vessel_Type = vessel_Type;
    }

    public String getVessel_Desc() {
        return Vessel_Desc;
    }

    public void setVessel_Desc(String vessel_Desc) {
        Vessel_Desc = vessel_Desc;
    }

    public String getVesselPassengerCapacity() {
        return VesselPassengerCapacity;
    }

    public void setVesselPassengerCapacity(String vesselPassengerCapacity) {
        VesselPassengerCapacity = vesselPassengerCapacity;
    }

    public String getVesselNumberOfCrew() {
        return VesselNumberOfCrew;
    }

    public void setVesselNumberOfCrew(String vesselNumberOfCrew) {
        VesselNumberOfCrew = vesselNumberOfCrew;
    }
}
