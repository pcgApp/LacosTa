package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas;

public class DataShippingProfileList {

    private String VesselName;
    private String VesselType;
    private String Origin;
    private String Destination;
    private String VesselPassengerCapacity;
    private String VesselNumberOfCrew;

    public DataShippingProfileList(){

    }

    public DataShippingProfileList(String VesselName, String Vesseltype, String Origin, String Destination,
                                   String VesselPassengerCapacity, String VesselNumberOfCrew) {
        this.VesselName = VesselName;
        this.VesselType = Vesseltype;
        this.Origin = Origin;
        this.Destination = Destination;
        this.VesselPassengerCapacity = VesselPassengerCapacity;
        this.VesselNumberOfCrew = VesselNumberOfCrew;

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

    public String getVesselName() {
        return VesselName;
    }

    public void setVesselName(String vesselName) {
        VesselName = vesselName;
    }

    public String getVesselType() {
        return VesselType;
    }

    public void setVesselType(String vesselType) {
        VesselType = vesselType;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }
}
